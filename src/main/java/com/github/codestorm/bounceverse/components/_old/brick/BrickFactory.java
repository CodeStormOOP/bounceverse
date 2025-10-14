package com.github.codestorm.bounceverse.components._old.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** Factory class responsible for creating various types of brick entities used in the game. */
public class BrickFactory implements EntityFactory {
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 30;
    private static final int DEFAULT_HP = 1;

    // Normal Brick.
    public static Entity newBrick(double x, double y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT))
                .with(
                        new BrickComponent(
                                DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP, Color.LIGHTBLUE))
                .build();
    }

    // Strong Brick.
    public static Entity newStrongBrick(double x, double y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT))
                .with(
                        new BrickComponent(
                                DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP * 3, Color.ORANGE))
                .build();
    }

    // Explode Brick.
    public static Entity newExplodeBrick(double x, double y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT))
                .with(new ExplodeBrick(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP, Color.RED))
                .build();
    }

    // Protected Brick.
    public static Entity newProtectedBrick(double x, double y, String shieldSide) {
        ProtectedBrick protectedBrick =
                new ProtectedBrick(
                        DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP, Color.WHITE, shieldSide);

        Rectangle body = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        body.setArcWidth(8);
        body.setArcHeight(8);

        Rectangle shield = null;
        double thickness = 8;

        switch (protectedBrick.getShieldSide().toLowerCase()) {
            case "top" -> shield = new Rectangle(-0.5, -1.5, DEFAULT_WIDTH + 1, thickness);
            case "bottom" ->
                    shield =
                            new Rectangle(
                                    -0.5,
                                    DEFAULT_HEIGHT - thickness + 1,
                                    DEFAULT_WIDTH + 1,
                                    thickness);
            case "left" -> shield = new Rectangle(-1.5, -0.5, thickness, DEFAULT_HEIGHT + 1);
            case "right" ->
                    shield =
                            new Rectangle(
                                    DEFAULT_WIDTH - thickness + 1,
                                    -0.5,
                                    thickness,
                                    DEFAULT_HEIGHT + 1);
        }

        if (shield != null) {
            shield.setFill(Color.GOLD);
        }

        Group view = new Group();
        if (shield == null) {
            view = new Group(body);
        } else {
            view = new Group(body, shield);
        }

        return FXGL.entityBuilder().at(x, y).viewWithBBox(view).with(protectedBrick).build();
    }
}
