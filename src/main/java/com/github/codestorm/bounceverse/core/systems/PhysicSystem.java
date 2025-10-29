package com.github.codestorm.bounceverse.core.systems;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Side;

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
                final var collisionDirection
                        = Utilities.Collision.getCollisionDirection(ball, brick);

                final var physics = ball.getComponent(PhysicsComponent.class);
                switch (collisionDirection) {
                    case UP, DOWN ->
                        physics.setLinearVelocity(
                                physics.getVelocityX(), -physics.getVelocityY());
                    case LEFT, RIGHT ->
                        physics.setLinearVelocity(
                                -physics.getVelocityX(), physics.getVelocityY());
                    default -> {
                    }
                }

                 var shieldOpt = brick.getComponentOptional(
                com.github.codestorm.bounceverse.components.properties.Shield.class
            );

            if (shieldOpt.isPresent()) {
                var shield = shieldOpt.get();

                // Kiểm tra hướng bị bảo vệ
                switch (collisionDirection) {
                    case UP -> {
                        if (shield.hasSide(Side.TOP)) return;
                    }
                    case DOWN -> {
                        if (shield.hasSide(Side.BOTTOM)) return;
                    }
                    case LEFT -> {
                        if (shield.hasSide(Side.LEFT)) return;
                    }
                    case RIGHT -> {
                        if (shield.hasSide(Side.RIGHT)) return;
                    }
                    default -> {}
                }
            }

                // damage
                final var atk = ball.getComponent(Attack.class);
                atk.execute(List.of(brick));
            }
        });

        // Ball vs Paddle
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BALL, EntityType.PADDLE) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity paddle) {
                final var collisionDirection
                        = Utilities.Collision.getCollisionDirection(ball, paddle);

                final var physics = ball.getComponent(PhysicsComponent.class);
                switch (collisionDirection) {
                    case UP, DOWN ->
                        physics.setLinearVelocity(
                                physics.getVelocityX(), -physics.getVelocityY());
                    case LEFT, RIGHT ->
                        physics.setLinearVelocity(
                                -physics.getVelocityX(), physics.getVelocityY());
                    default -> {
                    }
                }
            }
        });

        // Ball vs Wall
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BALL, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity wall) {
                final var physics = ball.getComponent(PhysicsComponent.class);
                String side = wall.getString("side");

                switch (side) {
                    case "LEFT", "RIGHT" ->
                        physics.setLinearVelocity(-physics.getVelocityX(), physics.getVelocityY());
                    case "TOP", "BOTTOM" ->
                        physics.setLinearVelocity(physics.getVelocityX(), -physics.getVelocityY());
                    default -> {
                        // fallback nếu chưa có side
                        final var dir = Utilities.Collision.getCollisionDirection(ball, wall);
                        switch (dir) {
                            case UP, DOWN ->
                                physics.setLinearVelocity(
                                        physics.getVelocityX(), -physics.getVelocityY());
                            case LEFT, RIGHT ->
                                physics.setLinearVelocity(
                                        -physics.getVelocityX(), physics.getVelocityY());
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
                    case "BOTTOM" ->
                        ball.translateY(-eps);
                }
            }
        });

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
