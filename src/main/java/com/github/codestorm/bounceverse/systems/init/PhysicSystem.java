package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleSizeManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpContainer;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

/**
 *
 *
 * <h1>{@link PhysicSystem}</h1>
 *
 * {@link System} quản lý Vật lý trong game. <br>
 * <i>Đây là một Singleton, cần lấy instance thông qua
 * {@link #getInstance()}</i>.
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 * @see InitialSystem
 */
public final class PhysicSystem extends InitialSystem {

    private PhysicSystem() {
    }

    public static PhysicSystem getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void apply() {
        final var physicWorld = FXGL.getPhysicsWorld();
        physicWorld.setGravity(0, 0);

        // Bullet vs Brick
        physicWorld.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity brick) {
                final var atk = bullet
                        .getComponentOptional(com.github.codestorm.bounceverse.components.behaviors.Attack.class);
                atk.ifPresent(a -> a.execute(java.util.List.of(brick)));
            }
        });

        // Ball vs Brick
        physicWorld.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                // An toàn khi thiếu PhysicsComponent
                final var physicsOpt = ball.getComponentOptional(PhysicsComponent.class);
                if (physicsOpt.isEmpty()) {
                    return;
                }
                final var physics = physicsOpt.get();

                // Xác định hướng va chạm
                final var dir = com.github.codestorm.bounceverse.Utilities.Collision.getCollisionDirection(ball, brick);

                // Kiểm tra shield (nếu có)
                var shieldOpt = brick
                        .getComponentOptional(com.github.codestorm.bounceverse.components.properties.Shield.class);
                if (shieldOpt.isPresent()) {
                    var shield = shieldOpt.get();

                    boolean isProtected = (dir == DirectionUnit.UP && shield.hasSide(javafx.geometry.Side.TOP))
                            || (dir == DirectionUnit.DOWN && shield.hasSide(javafx.geometry.Side.BOTTOM))
                            || (dir == DirectionUnit.LEFT && shield.hasSide(javafx.geometry.Side.LEFT))
                            || (dir == DirectionUnit.RIGHT && shield.hasSide(javafx.geometry.Side.RIGHT));

                    if (isProtected) {
                        // Nếu bị chắn, chỉ phản nảy mà không gây damage
                        switch (dir) {
                            case UP, DOWN ->
                                physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                            case LEFT, RIGHT ->
                                physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                            default -> {
                            }
                        }
                        return;
                    }
                }

                // Nếu không bị chắn → phản nảy + gây damage
                switch (dir) {
                    case UP, DOWN ->
                        physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                    case LEFT, RIGHT ->
                        physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                    default -> {
                    }
                }

                // Thực thi attack (gây damage)
                final var atkOpt = ball
                        .getComponentOptional(com.github.codestorm.bounceverse.components.behaviors.Attack.class);
                atkOpt.ifPresent(a -> a.execute(java.util.List.of(brick)));
            }
        });

        // Ball vs Paddle
        physicWorld.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.PADDLE) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity paddle) {
                var physics = ball.getComponent(PhysicsComponent.class);

                double paddleCenterX = paddle.getCenter().getX();
                double ballX = ball.getCenter().getX();
                double offset = (ballX - paddleCenterX) / (paddle.getWidth() / 2.0);
                offset = Math.max(-1.0, Math.min(1.0, offset));

                double angle = Math.toRadians(90 - 45 * offset);
                double speed = 300;

                double vx = speed * Math.cos(angle);
                double vy = -speed * Math.sin(angle);

                physics.setLinearVelocity(vx, vy);
            }
        });

        // Ball vs Wall
        physicWorld.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity wall) {
                final var physics = ball.getComponent(PhysicsComponent.class);
                String side = wall.getString("side");

                switch (side) {
                    case "LEFT", "RIGHT" ->
                        physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                    case "TOP" ->
                        physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                    case "BOTTOM" -> {
                        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).forEach(Entity::removeFromWorld);

                        FXGL.getGameTimer().runOnceAfter(() -> {
                            var paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);

                            var sizeManager = paddle.getComponentOptional(
                                    PaddleSizeManager.class);
                            sizeManager.ifPresent(PaddleSizeManager::resetSize);

                            PowerUpManager.getInstance().clearAll();

                            double x = paddle.getCenter().getX()
                                    - BallFactory.DEFAULT_RADIUS;
                            double y = paddle.getY()
                                    - BallFactory.DEFAULT_RADIUS * 2;

                            FXGL.spawn("ball", new SpawnData(x, y).put("attached", true));
                            FXGL.set("ballAttached", true);
                        }, javafx.util.Duration.millis(100));
                    }
                    default -> {
                        final var dir = com.github.codestorm.bounceverse.Utilities.Collision.getCollisionDirection(ball,
                                wall);
                        switch (dir) {
                            case UP, DOWN ->
                                physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                            case LEFT, RIGHT ->
                                physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                            default -> {
                            }
                        }
                    }
                }

                final double eps = 0.5;
                switch (side) {
                    case "LEFT" ->
                        ball.translateX(eps);
                    case "RIGHT" ->
                        ball.translateX(-eps);
                    case "TOP" ->
                        ball.translateY(eps);
                }
            }
        });

        // Paddle vs Wall
        physicWorld.addCollisionHandler(new CollisionHandler(EntityType.PADDLE, EntityType.WALL) {
            @Override
            protected void onCollision(Entity paddle, Entity wall) {
                String side = wall.getString("side");
                final double eps = 0.5;

                switch (side) {
                    case "LEFT" ->
                        paddle.setX(wall.getRightX() + eps);
                    case "RIGHT" ->
                        paddle.setX(wall.getX() - paddle.getWidth() - eps);
                    default -> {
                    }
                }
            }
        });

        // Paddle vs Power Up
        physicWorld.addCollisionHandler(new CollisionHandler(EntityType.PADDLE, EntityType.POWER_UP) {
            @Override
            protected void onCollisionBegin(Entity paddle, Entity powerUp) {
                powerUp.getComponentOptional(PowerUpContainer.class).ifPresent(container -> {
                    container.addTo(paddle);

                    // gọi apply() cho các Power-Up có hành vi kích hoạt
                    container.getContainer().values().forEach(comp -> {
                        if (comp instanceof PowerUp effect) {
                            effect.apply(paddle);
                        }
                    });
                });

                powerUp.removeFromWorld();
            }
        });

    }

    /**
     * Lazy-loaded singleton holder.
     */
    private static final class Holder {

        static final PhysicSystem INSTANCE = new PhysicSystem();
    }
}
