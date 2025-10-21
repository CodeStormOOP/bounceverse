package com.github.codestorm.bounceverse.factory.entities;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 *
 * <h1>{@link BulletFactory}</h1>
 *
 * Factory để tạo các entity loại {@link EntityType#BULLET} trong trò chơi.
 *
 * @see EntityFactory
 */
public final class BulletFactory implements EntityFactory {
    @Spawns("paddleBullet")
    public Entity newBullet(SpawnData data) {
        final var physics = new PhysicsComponent();
        physics.setLinearVelocity(((Vec2) data.get("velocity")).toPoint2D());

        return entityBuilder(data)
                .type(EntityType.BULLET)
                .viewWithBBox(new Circle(4, Color.YELLOW))
                .with(new CollidableComponent(true), physics)
                .build();
    }
}
