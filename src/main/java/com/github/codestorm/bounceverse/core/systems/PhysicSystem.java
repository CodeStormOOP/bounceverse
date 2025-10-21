package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.data.types.EntityType;
import java.util.List;

/**
 *
 *
 * <h1>{@link PhysicSystem}</h1>
 *
 * {@link System} quản lý Vật lý trong game. <br>
 * <i>Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}</i>.
 *
 * @see System
 */
public final class PhysicSystem extends System {
    private PhysicSystem() {}

    public static PhysicSystem getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void apply() {
        final var physicWorld = FXGL.getPhysicsWorld();

        // Bullet vs Brick
        physicWorld.addCollisionHandler(
                new CollisionHandler(EntityType.BULLET, EntityType.BRICK) {
                    @Override
                    protected void onCollisionBegin(Entity bullet, Entity brick) {
                        final var bulletAttack = bullet.getComponentOptional(Attack.class);
                        if (bulletAttack.isEmpty()) {
                            return;
                        }
                        bulletAttack.get().execute(List.of(brick));
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
