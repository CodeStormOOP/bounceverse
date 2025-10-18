package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.github.codestorm.bounceverse.components.properties.Velocity;
import com.github.codestorm.bounceverse.components.properties.Width;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaddleFactory implements EntityFactory {
    private static final double DEFAULT_WIDTH = 150;
    private static final double DEFAULT_HEIGHT = 20;

    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        Rectangle view = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        view.setArcWidth(14);
        view.setArcHeight(14);
        view.setFill(Color.LIGHTBLUE);

        return FXGL.entityBuilder(data)
                .type(EntityType.PADDLE)
                .view(view)
                .bbox(new HitBox(BoundingShape.box(DEFAULT_WIDTH, DEFAULT_HEIGHT)))
                .with(new CollidableComponent(true))
                .with(new Velocity(400))
                .with(new Width())
                .build();
    }
}
