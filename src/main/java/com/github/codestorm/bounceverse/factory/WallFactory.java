package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.github.codestorm.bounceverse.components.properties.Wall;
import com.github.codestorm.bounceverse.data.types.EntityType;
import com.github.codestorm.bounceverse.data.types.Side;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WallFactory implements EntityFactory {
    
    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        double width = data.get("width");
        double height = data.get("height");
        Side side = data.hasKey("side") ? data.get("side") : Side.LEFT;

        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .viewWithBBox(new Rectangle(width, height, Color.TRANSPARENT)) // không vẽ, chỉ hitbox
                .with(new CollidableComponent(true))
                .with(new Wall(side))
                .build();
    }
}
