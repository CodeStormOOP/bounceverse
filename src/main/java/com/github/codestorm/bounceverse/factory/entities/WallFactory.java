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
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 *
 * <h1>{@link WallFactory}</h1>
 *
 * <p>This class spawns {@link EntityType#WALL} entity for the game world.
 *
 * <p>By default, 4 walls top, left, bottom, right will be spawn at the beginning.
 */
public final class WallFactory extends EntityFactory {
    public static final double DEFAULT_THICKNESS = 5;
    public static final Color DEFAULT_COLOR = Color.GRAY;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        final var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        final Side side = data.get("side");

        final var thickness = Utilities.Typing.getOr(data, "thickness", DEFAULT_THICKNESS);
        final var color = Utilities.Typing.getOr(data, "color", DEFAULT_COLOR);

        Point2D pos = null;
        double width = 0;
        double height = 0;
        switch (side) {
            case TOP:
                pos = AnchorPoint.TOP_LEFT.of(appShape);
                width = appShape.getWidth();
                height = thickness;
                break;
            case BOTTOM:
                pos = AnchorPoint.BOTTOM_LEFT.of(appShape).subtract(0, thickness);
                width = appShape.getWidth();
                height = thickness;
                break;
            case LEFT:
                pos = AnchorPoint.TOP_LEFT.of(appShape);
                width = thickness;
                height = appShape.getHeight();
                break;
            case RIGHT:
                pos = AnchorPoint.TOP_RIGHT.of(appShape).subtract(thickness, 0);
                width = thickness;
                height = appShape.getHeight();
                break;
        }
        final var rect = new Rectangle(width, height, color);

        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);
        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(pos)
                .viewWithBBox(rect)
                .collidable()
                .with(physics)
                .with("side", side)
                .with("thickness", thickness);
    }

    /**
     * Create Top {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Top wall
     */
    @Spawns("wallTop")
    public Entity newWallTop(SpawnData data) {
        data.put("side", Side.TOP);
        return getBuilder(data).buildAndAttach();
    }

    /**
     * Create Bottom {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Botton wall
     */
    @Spawns("wallBottom")
    public Entity newWallBottom(SpawnData data) {
        data.put("side", Side.BOTTOM);
        return getBuilder(data).buildAndAttach();
    }

    /**
     * Create Left {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Left wall
     */
    @Spawns("wallLeft")
    public Entity newWallLeft(SpawnData data) {
        data.put("side", Side.LEFT);
        return getBuilder(data).buildAndAttach();
    }

    /**
     * Create Right {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Left wall
     */
    @Spawns("wallRight")
    public Entity newWallRight(SpawnData data) {
        data.put("side", Side.RIGHT);
        return getBuilder(data).buildAndAttach();
    }
}
