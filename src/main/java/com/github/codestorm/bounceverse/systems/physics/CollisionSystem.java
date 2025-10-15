package com.github.codestorm.bounceverse.systems.physics;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.github.codestorm.bounceverse.components._old.ball.BallComponent;
import com.github.codestorm.bounceverse.components.properties.brick.BrickHealth;
import com.github.codestorm.bounceverse.data.types.EntityType;
import com.github.codestorm.bounceverse.systems.System;

/**
 *
 *
 * <h1>{@link CollisionSystem}</h1>
 *
 * Hệ thống xử lý va chạm.
 *
 * <p><i>Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}</i>.
 *
 * @see System
 */
public final class CollisionSystem extends System {
    /**
     * Lazy-loaded singleton holder.
     *
     * <p>Follow <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final CollisionSystem INSTANCE = new CollisionSystem();
    }

    public static CollisionSystem getInstance() {
        return Holder.INSTANCE;
    }

    // ? Tạo các Group CollisionHandler ở đây (dùng composition)

    @Override
    public void apply() {
        // ? Viết các logic CollisionHandler ở đây. eg:
        //        getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER,
        // EntityType.COIN) {
        //            @Override
        //            protected void onCollisionBegin(Entity player, Entity coin) {
        //                coin.removeFromWorld();
        //            }
        //        });

        PhysicsWorld physics = FXGL.getPhysicsWorld();

        physics.addCollisionHandler(
                new CollisionHandler(EntityType.BALL, EntityType.BRICK) {
                    @Override
                    protected void onCollisionBegin(Entity ball, Entity brick) {
                        // decrease hp on collision
                        var brickHealth = brick.getComponent(BrickHealth.class);
                        brickHealth.getHealth().damage(1);

                        // bounce
                        ball.getComponent(BallComponent.class).bounce();
                    }
                });
    }

    private CollisionSystem() {}
}
