package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.typing.enums.AnchorPoint;
import com.github.codestorm.bounceverse.typing.enums.CollisionGroup;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** Factory tạo 4 tường vật lý bao quanh màn chơi. */
public final class WallFactory extends EntityFactory {

    public static final double DEFAULT_THICKNESS = 5;
    public static final Color DEFAULT_COLOR = Color.GRAY;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        Side side = data.get("side");

        double thickness = Utilities.Typing.getOr(data, "thickness", DEFAULT_THICKNESS);
        var color = Utilities.Typing.getOr(data, "color", DEFAULT_COLOR);

        Point2D pos;
        double width, height;

        switch (side) {
            case TOP -> {
                pos = AnchorPoint.TOP_LEFT.of(appShape);
                width = appShape.getWidth();
                height = thickness;
            }
            case BOTTOM -> {
                pos = AnchorPoint.BOTTOM_LEFT.of(appShape).subtract(0, thickness);
                width = appShape.getWidth();
                height = thickness;
            }
            case LEFT -> {
                pos = AnchorPoint.TOP_LEFT.of(appShape);
                width = thickness;
                height = appShape.getHeight();
            }
            case RIGHT -> {
                pos = AnchorPoint.TOP_RIGHT.of(appShape).subtract(thickness, 0);
                width = thickness;
                height = appShape.getHeight();
            }
            default -> throw new IllegalArgumentException("Invalid side for wall: " + side);
        }

        var rect = new Rectangle(width, height, color);
        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);

        // Lọc va chạm: Tường thuộc nhóm WALL và va chạm với BALL, BULLET, PADDLE
        fixture.getFilter().categoryBits = CollisionGroup.WALL.bits;
        fixture.getFilter().maskBits =
                CollisionGroup.BALL.bits | CollisionGroup.BULLET.bits | CollisionGroup.PADDLE.bits;

        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(pos)
                .viewWithBBox(rect)
                .collidable()
                .with(physics)
                .with("side", side);
    }

    /** Tạo tường trên ({@link EntityType#WALL}). */
    @Spawns("wallTop")
    public Entity newWallTop(SpawnData data) {
        data.put("side", Side.TOP);
        return getBuilder(data).buildAndAttach();
    }

    /** Tạo tường dưới ({@link EntityType#WALL}). */
    @Spawns("wallBottom")
    public Entity newWallBottom(SpawnData data) {
        data.put("side", Side.BOTTOM);
        return getBuilder(data).buildAndAttach();
    }

    /** Tạo tường trái ({@link EntityType#WALL}). */
    @Spawns("wallLeft")
    public Entity newWallLeft(SpawnData data) {
        data.put("side", Side.LEFT);
        return getBuilder(data).buildAndAttach();
    }

    /** Tạo tường phải ({@link EntityType#WALL}). */
    @Spawns("wallRight")
    public Entity newWallRight(SpawnData data) {
        data.put("side", Side.RIGHT);
        return getBuilder(data).buildAndAttach();
    }
}
