package com.github.codestorm.bounceverse.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.github.codestorm.bounceverse.gameManager.BounceverseType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaddleFactory implements EntityFactory {
    private static final double DEFAULT_WIDTH = 150;
    private static final double DEFAULT_HEIGHT = 20;
    private static final double DEFAULT_SPEED = 400;

    // Paddle.
    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BounceverseType.PADDLE)
                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.DODGERBLUE))
                .with(new CollidableComponent(true))
                .with(new PaddleComponent(DEFAULT_SPEED))
                .build();
    }

    // Shrink Paddle.
    @Spawns("shrinkPaddle")
    public Entity newShrinkPaddle(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BounceverseType.PADDLE)
                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.ORANGERED))
                .with(new CollidableComponent(true))
                .with(new ShrinkPaddle(DEFAULT_SPEED))
                .build();
    }

    // Expand Paddle.
    @Spawns("expandPaddle")
    public Entity newExpandPaddle(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BounceverseType.PADDLE)
                .viewWithBBox(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.LIMEGREEN))
                .with(new CollidableComponent(true))
                .with(new ExpandPaddle(DEFAULT_SPEED))
                .build();
    }

    // Bullet Paddle.
    @Spawns("bulletPaddle")
    public Entity newBulletPaddle(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(BounceverseType.PADDLE)
                .viewWithBBox(new Rectangle(100, 20, Color.HOTPINK))
                .with(new CollidableComponent(true))
                .with(new LaserPaddle(400))
                .build();
    }
}
