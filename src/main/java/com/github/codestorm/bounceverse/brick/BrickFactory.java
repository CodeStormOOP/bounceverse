package com.github.codestorm.bounceverse.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.github.codestorm.bounceverse.gameManager.BounceverseType;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Factory class responsible for creating various types of brick entities used
 * in the game.
 */
public class BrickFactory implements EntityFactory {

        private static final int DEFAULT_WIDTH = 80;
        private static final int DEFAULT_HEIGHT = 30;
        private static final int DEFAULT_HP = 1;

        /** Normal brick. */
        @Spawns("normalBrick")
        public Entity newNormalBrick(SpawnData data) {
                return FXGL.entityBuilder(data)
                                .type(BounceverseType.BRICK)
                                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.LIGHTBLUE))
                                .with(new CollidableComponent(true))
                                .with(new BrickComponent(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP, Color.LIGHTBLUE))
                                .build();
        }

        /** Strong brick with higher HP. */
        @Spawns("strongBrick")
        public Entity newStrongBrick(SpawnData data) {
                return FXGL.entityBuilder(data)
                                .type(BounceverseType.BRICK)
                                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.ORANGE))
                                .with(new CollidableComponent(true))
                                .with(new BrickComponent(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP * 3, Color.ORANGE))
                                .build();
        }

        /** Explosive brick that damages nearby bricks when destroyed. */
        @Spawns("explodeBrick")
        public Entity newExplodeBrick(SpawnData data) {
                return FXGL.entityBuilder(data)
                                .type(BounceverseType.BRICK)
                                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.RED))
                                .with(new CollidableComponent(true))
                                .with(new ExplodeBrick(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP, Color.RED))
                                .build();
        }

        /** Protected brick with shield on one side. */
        @Spawns("protectedBrick")
        public Entity newProtectedBrick(SpawnData data) {
                String shieldSide = data.get("shieldSide");

                ProtectedBrick protectedBrick = new ProtectedBrick(
                                DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HP, Color.WHITE, shieldSide);

                Rectangle body = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
                body.setArcWidth(8);
                body.setArcHeight(8);

                Rectangle shield = null;
                double thickness = 8;

                switch (shieldSide.toLowerCase()) {
                        case "top" -> shield = new Rectangle(-1, -2, DEFAULT_WIDTH + 1, thickness);
                        case "bottom" -> shield = new Rectangle(-1, DEFAULT_HEIGHT - thickness + 2, DEFAULT_WIDTH + 2,
                                        thickness);
                        case "left" -> shield = new Rectangle(-2, -1, thickness, DEFAULT_HEIGHT + 2);
                        case "right" -> shield = new Rectangle(DEFAULT_WIDTH - thickness + 2, -1, thickness,
                                        DEFAULT_HEIGHT + 1);
                }

                if (shield != null)
                        shield.setFill(Color.GOLD);

                Group view = (shield != null) ? new Group(body, shield) : new Group(body);

                return FXGL.entityBuilder(data)
                                .type(BounceverseType.BRICK)
                                .viewWithBBox(view)
                                .with(new CollidableComponent(true))
                                .with(protectedBrick)
                                .build();
        }
}
