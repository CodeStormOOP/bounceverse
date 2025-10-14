package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.github.codestorm.bounceverse.components.properties.Move;
import com.github.codestorm.bounceverse.components.properties.Width;
import com.github.codestorm.bounceverse.data.types.EntityType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaddleFactory implements EntityFactory{
    private static final double  DEFAULT_WIDTH = 150;
    private static final double DEFAULT_HEIGHT = 20;

    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.PADDLE)
                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.DEEPSKYBLUE))
                .with(new CollidableComponent(true))
                .with(new Move(400)) // speed = 400 px/s
                .with(new Width())               // lưu kích thước gốc để expand/shrink
                .build();
    }
}
