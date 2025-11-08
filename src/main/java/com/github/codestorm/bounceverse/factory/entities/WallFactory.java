package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.typing.enums.AnchorPoint;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

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
 *
 * @author minngoc1213
 */
public final class WallFactory extends EntityFactory {
    public static final double DEFAULT_THICKNESS = 5;
    public static final Color DEFAULT_COLOR = Color.GRAY;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();

        fixture.setFriction(0f);
        fixture.setRestitution(1f);
        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder()
                .type(EntityType.WALL)
                .collidable()
                .with(physics)
                .anchorFromCenter();
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

        final double thickness =
                data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
        final Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

        final var pos = AnchorPoint.TOP_LEFT.of(appShape);
        final var width = appShape.getWidth();
        final var height = thickness;

        final var rect = new Rectangle(width, height, color);
        return getBuilder(data).at(pos).viewWithBBox(rect).buildAndAttach();
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

        final double thickness =
                data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
        final Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

        final var pos = AnchorPoint.BOTTOM_LEFT.of(appShape).subtract(0, thickness);
        final var width = appShape.getWidth();
        final var height = thickness;

        final var rect = new Rectangle(width, height, color);
        return getBuilder(data).at(pos).viewWithBBox(rect).buildAndAttach();
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

        final double thickness =
                data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
        final Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

        final var pos = AnchorPoint.TOP_LEFT.of(appShape);
        final var width = thickness;
        final var height = appShape.getHeight();

        final var rect = new Rectangle(width, height, color);
        return getBuilder(data).at(pos).viewWithBBox(rect).buildAndAttach();
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

        final double thickness =
                data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
        final Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

        final var pos = AnchorPoint.TOP_RIGHT.of(appShape).subtract(thickness, 0);
        final var width = thickness;
        final var height = appShape.getHeight();

        final var rect = new Rectangle(width, height, color);
        return getBuilder(data).at(pos).viewWithBBox(rect).buildAndAttach();
    }
}
