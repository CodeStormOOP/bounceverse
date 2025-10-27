package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.shape.Rectangle;

public class WallFactory implements EntityFactory {
    private static final double WALL_THICKNESS = 20;

    @Spawns("wallLeft")
    public Entity newWallLeft(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .at(0, 0)
                .viewWithBBox(new Rectangle(WALL_THICKNESS, FXGL.getAppHeight()))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("wallRight")
    public Entity newWallRight(SpawnData data) {
        double x = FXGL.getAppWidth() - WALL_THICKNESS;
        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .at(x, 0)
                .viewWithBBox(new Rectangle(WALL_THICKNESS, FXGL.getAppHeight()))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("wallTop")
    public Entity newWallTop(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .at(0, 0)
                .viewWithBBox(new Rectangle(FXGL.getAppWidth(), WALL_THICKNESS))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("wallBottom")
    public Entity newWallBottom(SpawnData data) {
        double y = FXGL.getAppHeight() - WALL_THICKNESS;
        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .at(0, y)
                .viewWithBBox(new Rectangle(FXGL.getAppWidth(), WALL_THICKNESS))
                .with(new CollidableComponent(true))
                .build();
    }
}
