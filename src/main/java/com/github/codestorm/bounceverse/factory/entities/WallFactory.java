package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.typing.enums.AnchorPoint;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 *
 * <h1>{@link WallFactory}</h1>
 *
 * <p>
 * This class spawns {@link EntityType#WALL} entity for the game world.
 *
 * <p>
 * By default, 4 walls top, left, bottom, right will be spawn at the beginning.
 *
 * @author minngoc1213
 */
public final class WallFactory implements EntityFactory {

    public static final double DEFAULT_THICKNESS = 5;

    /**
     * Create new Entity Wall with ing-game physic.
     *
     * @param pos position
     * @param width width
     * @param height height
     * @param side side
     * @return Wall entity at pos
     */
    public static Entity createWall(Point2D pos, double width, double height, String side) {
        Rectangle rect = new Rectangle(width, height, Color.GRAY);

        PhysicsComponent physics = new PhysicsComponent();

        FixtureDef fixture = new FixtureDef();
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
                .build();
    }

    /**
     * Create Top {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Top wall
     */
    @Spawns("wallTop")
    public Entity newWallTop(SpawnData data) {
        final var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        final Double thicknessObj = data.hasKey("thickness") ? data.get("thickness") : null;
        final double thickness = thicknessObj == null ? DEFAULT_THICKNESS : thicknessObj;

        return createWall(
                AnchorPoint.TOP_LEFT.of(appShape),
                appShape.getWidth(),
                thickness, "TOP");
    }

    /**
     * Create Bottom {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Botton wall
     */
    @Spawns("wallBottom")
    public Entity newWallBottom(SpawnData data) {
        final var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        final Double thicknessObj = data.hasKey("thickness") ? data.get("thickness") : null;
        final double thickness = thicknessObj == null ? DEFAULT_THICKNESS : thicknessObj;

        return createWall(
                AnchorPoint.BOTTOM_LEFT.of(appShape).subtract(0, thickness),
                appShape.getWidth(),
                thickness, "BOTTOM");
    }

    /**
     * Create Left {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Left wall
     */
    @Spawns("wallLeft")
    public Entity newWallLeft(SpawnData data) {
        final var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        final Double thicknessObj = data.hasKey("thickness") ? data.get("thickness") : null;
        final double thickness = thicknessObj == null ? DEFAULT_THICKNESS : thicknessObj;

        return createWall(
                AnchorPoint.TOP_LEFT.of(appShape),
                thickness,
                appShape.getHeight(), "LEFT");
    }

    /**
     * Create Right {@link EntityType#WALL}. `
     *
     * @param data Spawn data
     * @return Left wall
     */
    @Spawns("wallRight")
    public Entity newWallRight(SpawnData data) {
        final var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        final Double thicknessObj = data.hasKey("thickness") ? data.get("thickness") : null;
        final double thickness = thicknessObj == null ? DEFAULT_THICKNESS : thicknessObj;

        return createWall(
                AnchorPoint.TOP_RIGHT.of(appShape).subtract(thickness, 0),
                thickness,
                appShape.getHeight(), "RIGHT");
    }
}
