package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 *
 * <h1><b>Wall Factory</b></h1>
 *
 * <p>This class spawns Wall entity for the game world.
 *
 * <p>By default, 4 walls top, left, bottom, right will be spawn at the beginning.
 *
 * @author minngoc1213
 */
public class WallFactory implements EntityFactory {

    /**
     * Create new Entity Wall with ing-game physic.
     *
     * @param x coordinate X of wall
     * @param y coordinate Y of wall
     * @param width width of wall
     * @param height height of wall
     * @return Wall entity at (x, y)
     */
    public static Entity createWall(double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(width, height);
        rect.setFill(Color.GRAY);

        PhysicsComponent physics = new PhysicsComponent();

        FixtureDef fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);

        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder()
                .at(x, y)
                .view(rect)
                .bbox(BoundingShape.box(width, height))
                .with(physics)
                .with(new CollidableComponent(true))
                .anchorFromCenter()
                .build();
    }

    /** Spawn 4 walls top, left, bottom, right. */
    public static void spawnWalls() {
        double w = FXGL.getAppWidth();
        double h = FXGL.getAppHeight();

        double thickness = 5;

        FXGL.getGameWorld().addEntity(createWall(0, 0, w, thickness));
        FXGL.getGameWorld().addEntity(createWall(0, h - thickness, w, thickness));
        FXGL.getGameWorld().addEntity(createWall(0, 0, thickness, h));
        FXGL.getGameWorld().addEntity(createWall(w - thickness, 0, thickness, h));
    }
}
