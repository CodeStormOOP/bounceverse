package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.github.codestorm.bounceverse.components.behaviors.paddle.PaddleShooting;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleSizeManager;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 *
 * <h1>{@link PaddleFactory}</h1>
 *
 * Factory để tạo các entity loại {@link EntityType#PADDLE} trong trò chơi.
 *
 * @see EntityFactory
 */
public final class PaddleFactory implements EntityFactory {
    private static final double DEFAULT_WIDTH = 150;
    private static final double DEFAULT_HEIGHT = 20;
    private static final double DEFAULT_ARC_WIDTH = 14;
    private static final double DEFAULT_ARC_HEIGHT = 14;
    private static final Duration DEFAULT_SHOOT_COOLDOWN = Duration.ZERO;

    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        Rectangle view = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.LIGHTBLUE);
        view.setArcWidth(DEFAULT_ARC_WIDTH);
        view.setArcHeight(DEFAULT_ARC_HEIGHT);

        return FXGL.entityBuilder(data)
                .type(EntityType.PADDLE)
                .viewWithBBox(view)
                .collidable()
                .with(new PaddleShooting(DEFAULT_SHOOT_COOLDOWN))
                .with(new PaddleSizeManager())
                .build();
        // TODO: Thêm Dashing
    }
}
