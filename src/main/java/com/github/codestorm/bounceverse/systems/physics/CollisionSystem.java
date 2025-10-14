package com.github.codestorm.bounceverse.systems.physics;

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
    }

    private CollisionSystem() {}
}
