package com.github.codestorm.bounceverse.factory.entities;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
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
public final class BulletFactory extends EntityFactory {
    public static final double DEFAULT_RADIUS = 4;
    public static final Color DEFAULT_COLOR = Color.YELLOW;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        final var physics = new PhysicsComponent();
        physics.setLinearVelocity(((Vec2) data.get("velocity")).toPoint2D());

        return entityBuilder(data).type(EntityType.BULLET).collidable().with(physics);
    }

    @Spawns("paddleBullet")
    public Entity newBullet(SpawnData data) {
        final Point2D pos = data.get("pos");
        final double radius = Utilities.Typing.getOr(data, "radius", DEFAULT_RADIUS);
        final var color = Utilities.Typing.getOr(data, "color", DEFAULT_COLOR);

        final var bbox = new Circle(radius, color);
        return getBuilder(data).at(pos).viewWithBBox(bbox).buildAndAttach();
    }
}
