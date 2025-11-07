package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import java.util.List;

/**
 *
 *
 * <h1>{@link PhysicSystem}</h1>
 *
 * {@link InitialSystem} quản lý Vật lý trong game. <br>
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 * @see InitialSystem
 */
public final class PhysicSystem extends InitialSystem {
    private PhysicSystem() {}

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
                        final var collisionDirection =
                                Utilities.Collision.getCollisionDirection(ball, brick);

                        final var physics = ball.getComponent(PhysicsComponent.class);
                        switch (collisionDirection) {
                            case UP, DOWN ->
                                    physics.setLinearVelocity(
                                            physics.getVelocityX(), -physics.getVelocityY());
                            case LEFT, RIGHT ->
                                    physics.setLinearVelocity(
                                            -physics.getVelocityX(), physics.getVelocityY());
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
                        final var collisionDirection =
                                Utilities.Collision.getCollisionDirection(ball, paddle);

                        final var physics = ball.getComponent(PhysicsComponent.class);
                        switch (collisionDirection) {
                            case UP, DOWN ->
                                    physics.setLinearVelocity(
                                            physics.getVelocityX(), -physics.getVelocityY());
                            case LEFT, RIGHT ->
                                    physics.setLinearVelocity(
                                            -physics.getVelocityX(), physics.getVelocityY());
                        }
                    }
                });

        // Ball vs Wall
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BALL, EntityType.WALL) {
                    @Override
                    protected void onCollisionBegin(Entity ball, Entity wall) {
                        final var collisionDirection =
                                Utilities.Collision.getCollisionDirection(ball, wall);

                        final var physics = ball.getComponent(PhysicsComponent.class);
                        switch (collisionDirection) {
                            case UP, DOWN ->
                                    physics.setLinearVelocity(
                                            physics.getVelocityX(), -physics.getVelocityY());
                            case LEFT, RIGHT ->
                                    physics.setLinearVelocity(
                                            -physics.getVelocityX(), physics.getVelocityY());
                        }
                    }
                });
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final PhysicSystem INSTANCE = new PhysicSystem();
    }
}
