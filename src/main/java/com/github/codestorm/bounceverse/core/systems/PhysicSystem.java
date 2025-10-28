package com.github.codestorm.bounceverse.core.systems;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.Utils;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.core.GameVars;
import com.github.codestorm.bounceverse.data.types.DirectionUnit;
import com.github.codestorm.bounceverse.data.types.EntityType;

import javafx.util.Duration;

/**
 *
 *
 * <h1>{@link PhysicSystem}</h1>
 *
 * {@link System} quản lý Vật lý trong game. <br>
 * <i>Đây là một Singleton, cần lấy instance thông qua
 * {@link #getInstance()}</i>.
 *
 * @see System
 */
public final class PhysicSystem extends System {

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
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BULLET, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity brick) {
                final var atk = bullet.getComponentOptional(Attack.class);
                if (atk.isEmpty()) {
                    return;
                }
                atk.get().execute(List.of(brick));
            }
        });

        // Ball vs Brick
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BALL, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                // collision
                final var physics = ball.getComponent(PhysicsComponent.class);

                final var collisionDirection = Utils.Collision.getCollisionDirection(ball, brick);

                if (collisionDirection == null) {
                    // Fallback theo overlap depth
                    double dx = ball.getCenter().getX() - brick.getCenter().getX();
                    double dy = ball.getCenter().getY() - brick.getCenter().getY();
                    double overlapX = (ball.getWidth() + brick.getWidth()) / 2.0 - Math.abs(dx);
                    double overlapY = (ball.getHeight() + brick.getHeight()) / 2.0 - Math.abs(dy);

                    if (overlapX < overlapY) {
                        // Va ngang mạnh hơn -> lật X
                        physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                    } else {
                        // Va dọc mạnh hơn -> lật Y
                        physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                    }

                    // damage
                    ball.getComponent(Attack.class).execute(List.of(brick));
                    return;
                }

                switch (collisionDirection) {
                    case UP, DOWN ->
                        physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                    case LEFT, RIGHT ->
                        physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                }

                // damage
                ball.getComponent(Attack.class).execute(List.of(brick));
            }
        });

        // Ball vs Paddle
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BALL, EntityType.PADDLE) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity paddle) {
                final var physics = ball.getComponent(PhysicsComponent.class);

                double vx = physics.getVelocityX();
                double vy = physics.getVelocityY();
                if (vx == 0 && vy == 0) {
                    return;
                }

                double ballCenterX = ball.getCenter().getX();
                double paddleCenterX = paddle.getCenter().getX();
                double halfPaddleWidth = paddle.getWidth() / 2.0;
                double relative = 0;
                if (halfPaddleWidth != 0) {
                    relative = (ballCenterX - paddleCenterX) / halfPaddleWidth; // -1 .. 1
                    if (relative < -1) {
                        relative = -1;
                    }
                    if (relative > 1) {
                        relative = 1;
                    }
                }

                double speed = Math.hypot(vx, vy);
                double maxBounceAngle = Math.toRadians(75);
                double angle = relative * maxBounceAngle;

                double newVx = speed * Math.sin(angle);
                double newVy = -Math.abs(speed * Math.cos(angle));

                physics.setLinearVelocity(newVx, newVy);
            }
        });

        // Ball vs Wall
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BALL, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity wall) {
                final DirectionUnit collisionDirection = Utils.Collision.getCollisionDirection(ball, wall);

                final var physics = ball.getComponent(PhysicsComponent.class);
                double vx = physics.getVelocityX();
                double vy = physics.getVelocityY();

                if (collisionDirection == null) {
                    return;
                }

                switch (collisionDirection) {
                    case UP -> {
                        // chạm trần -> đi xuống
                        physics.setLinearVelocity(vx, Math.abs(vy));
                    }
                    case DOWN -> {
                        int lives = GameVars.decrementLives();

                        physics.setLinearVelocity(0, 0);

                        // reset ball lên giữa paddle
                        var paddles = FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE);
                        if (!paddles.isEmpty()) {
                            Entity paddle = paddles.iterator().next();
                            double px = paddle.getCenter().getX();
                            double py = paddle.getY();
                            double bw = ball.getWidth();
                            double bh = ball.getHeight();

                            ball.setPosition(px - bw / 2.0, py - bh - 2.0);
                        }

                        if (lives <= 0) {
                            FXGL.getGameController().exit();
                            return;
                        }

                        double speed = Math.max(150, Math.hypot(vx, vy));
                        FXGL.getGameTimer().runOnceAfter(()
                                -> ball.getComponent(PhysicsComponent.class)
                                        .setLinearVelocity(0, -Math.abs(speed)),
                                new Duration(200)
                        );
                    }
                    case LEFT -> {
                        // 1) Ngắt X để tránh engine override
                        physics.setLinearVelocity(0, vy);

                        // 2) Đặt ra ngoài tường (dựa vào biên phải của wall)
                        ball.setX(wall.getRightX() + 2.0);

                        // 3) Sau 5ms, set lại tốc độ với độ lớn ổn định
                        FXGL.getGameTimer().runOnceAfter(() -> {
                            double speed = Math.max(150, Math.hypot(vx, vy));
                            physics.setLinearVelocity(Math.abs(speed), vy);
                        }, Duration.millis(5));
                    }
                    case RIGHT -> {
                        physics.setLinearVelocity(0, vy);

                        // biên trái của wall trừ bề rộng ball
                        ball.setX(wall.getX() - ball.getWidth() - 2.0);

                        FXGL.getGameTimer().runOnceAfter(() -> {
                            double speed = Math.max(150, Math.hypot(vx, vy));
                            physics.setLinearVelocity(-Math.abs(speed), vy);
                        }, Duration.millis(5));
                    }
                    default -> {
                    }
                }
            }
        }
        );

        // Paddle vs Wall 
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.PADDLE, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity paddle, Entity wall) {
                // Tính hướng va chạm
                final var dir = Utils.Collision.getCollisionDirection(paddle, wall);
                if (dir == null) {
                    return;
                }

                switch (dir) {
                    case LEFT -> {
                        // Paddle va vào tường trái -> đặt sát mép phải của wall
                        paddle.setX(wall.getRightX());
                    }
                    case RIGHT -> {
                        // Paddle va vào tường phải -> đặt sát mép trái của wall
                        paddle.setX(wall.getX() - paddle.getWidth());
                    }
                    default -> {
                        // Bỏ qua UP/DOWN cho paddle
                    }
                }
            }
        }
        );

    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow
     * <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {

        static final PhysicSystem INSTANCE = new PhysicSystem();
    }
}
