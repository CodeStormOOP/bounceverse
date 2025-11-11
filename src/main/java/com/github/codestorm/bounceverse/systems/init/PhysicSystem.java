package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.properties.Shield;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleSizeManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpContainer;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Side;

public final class PhysicSystem extends InitialSystem {

    private PhysicSystem() {}

    public static PhysicSystem getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void apply() {
        var world = FXGL.getPhysicsWorld();
        world.setGravity(0, 0);

        // Bullet vs Brick
        world.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity brick) {
                bullet.getComponentOptional(com.github.codestorm.bounceverse.components.behaviors.Attack.class)
                        .ifPresent(a -> a.execute(java.util.List.of(brick)));
            }
        });

        // Ball vs Brick
        world.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                var physics = ball.getComponent(PhysicsComponent.class);
                var dir = Utilities.Collision.getCollisionDirection(ball, brick);

                // Shield check
                var shieldOpt = brick.getComponentOptional(Shield.class);
                if (shieldOpt.isPresent() && shieldOpt.get().hasSide(dir.toSide())) {
                    bounce(physics, dir);
                    return;
                }

                bounce(physics, dir);
                ball.getComponentOptional(com.github.codestorm.bounceverse.components.behaviors.Attack.class)
                        .ifPresent(a -> a.execute(java.util.List.of(brick)));
            }
        });

        // Ball vs Paddle
        world.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.PADDLE) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity paddle) {
                var physics = ball.getComponent(PhysicsComponent.class);
                double offset = (ball.getCenter().getX() - paddle.getCenter().getX()) / (paddle.getWidth() / 2.0);
                offset = Math.max(-1.0, Math.min(1.0, offset));
                double angle = Math.toRadians(90 - 45 * offset);
                double speed = 300;
                physics.setLinearVelocity(speed * Math.cos(angle), -speed * Math.sin(angle));
            }
        });

        // Ball vs Wall
        world.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity wall) {
                var physics = ball.getComponent(PhysicsComponent.class);
                Side side = wall.getObject("side");

                switch (side) {
                    case LEFT, RIGHT -> physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                    case TOP -> physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                    case BOTTOM -> {
                        ball.removeFromWorld();
                        FXGL.getGameTimer().runOnceAfter(() -> {
                            if (FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).isEmpty()) {
                                var paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
                                paddle.getComponentOptional(PaddleSizeManager.class)
                                        .ifPresent(PaddleSizeManager::resetSize);
                                PowerUpManager.getInstance().clearAll();

                                double x = paddle.getCenter().getX() - BallFactory.DEFAULT_RADIUS;
                                double y = paddle.getY() - BallFactory.DEFAULT_RADIUS * 2;
                                FXGL.spawn("ball", new SpawnData(x, y).put("attached", true));
                                FXGL.set("ballAttached", true);
                            }
                        }, javafx.util.Duration.millis(100));
                    }
                }
            }
        });

        // Paddle vs Wall
        world.addCollisionHandler(new CollisionHandler(EntityType.PADDLE, EntityType.WALL) {
            @Override
            protected void onCollision(Entity paddle, Entity wall) {
                Side side = wall.getObject("side");
                if (side == Side.LEFT)
                    paddle.setX(wall.getRightX());
                else if (side == Side.RIGHT)
                    paddle.setX(wall.getX() - paddle.getWidth());
            }
        });

        // Paddle vs PowerUp
        world.addCollisionHandler(new CollisionHandler(EntityType.PADDLE, EntityType.POWER_UP) {
            @Override
            protected void onCollisionBegin(Entity paddle, Entity powerUp) {
                if (paddle.isActive() && powerUp.isActive()) {
                    powerUp.getComponentOptional(PowerUpContainer.class).ifPresent(container -> {
                        container.addTo(paddle);
                        container.getContainer().values().forEach(c -> {
                            if (c instanceof PowerUp p) p.apply(paddle);
                        });
                    });
                    powerUp.removeFromWorld();
                }
            }
        });
    }

    private static void bounce(PhysicsComponent phys, DirectionUnit dir) {
        switch (dir) {
            case UP, DOWN -> phys.setLinearVelocity(phys.getVelocityX(), -phys.getVelocityY());
            case LEFT, RIGHT -> phys.setLinearVelocity(-phys.getVelocityX(), phys.getVelocityY());
        }
    }

    private static final class Holder {
        static final PhysicSystem INSTANCE = new PhysicSystem();
    }
}
