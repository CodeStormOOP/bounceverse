package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.github.codestorm.bounceverse.components.properties.Velocity;
import com.github.codestorm.bounceverse.components.properties.brick.BrickHealth;
import com.github.codestorm.bounceverse.components.properties.wall.Border;
import com.github.codestorm.bounceverse.data.types.EntityType;

/**
 *
 *
 * <h1>{@link PhysicSystem}</h1>
 *
 * Hệ thống xử lý vật lý.
 *
 * <p><i>Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}</i>.
 *
 * @see System
 */
public final class PhysicSystem extends System {

    private PhysicSystem() {}

    public static PhysicSystem getInstance() {
        return Holder.INSTANCE;
    }

    // ? Tạo các Group CollisionHandler ở đây (dùng composition)
    @Override
    public void apply() {
        // Bullet vs Brick
        FXGL.getPhysicsWorld()
                .addCollisionHandler(
                        new CollisionHandler(EntityType.BULLET, EntityType.BRICK) {
                            @Override
                            protected void onCollisionBegin(Entity bullet, Entity brick) {
                                // Nếu brick có máu -> trừ 1 HP
                                if (brick.hasComponent(BrickHealth.class)) {
                                    brick.getComponent(BrickHealth.class).damage(1);
                                    // TODO: kiểm tra shield
                                }

                                // Hủy viên đạn
                                bullet.removeFromWorld();
                            }
                        });

        // Paddle vs Wall
        FXGL.getPhysicsWorld()
                .addCollisionHandler(
                        new CollisionHandler(EntityType.PADDLE, EntityType.WALL) {
                            @Override
                            protected void onCollision(Entity paddle, Entity wall) {
                                if (wall.hasComponent(Border.class)) {
                                    var wallProp = wall.getComponent(Wall.class);
                                    var move = paddle.getComponentOptional(Velocity.class);

                                    move.ifPresent(m -> m.setVector(0));

                                    switch (wallProp.getSide()) {
                                        case LEFT -> paddle.setX(wall.getRightX());
                                        case RIGHT -> paddle.setX(wall.getX() - paddle.getWidth());
                                        default ->
                                                throw new IllegalArgumentException(
                                                        "Unexpected value: " + wallProp.getSide());
                                    }
                                }
                            }
                        });
    }

    /**
     * Lazy-loaded singleton holder.
     *
     * <p>Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final PhysicSystem INSTANCE = new PhysicSystem();
    }
}
