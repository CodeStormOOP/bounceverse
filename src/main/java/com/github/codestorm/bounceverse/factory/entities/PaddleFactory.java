package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.paddle.PaddleShooting;
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
public final class PaddleFactory extends EntityFactory {
    private static final double DEFAULT_WIDTH = 150;
    private static final double DEFAULT_HEIGHT = 20;
    private static final double DEFAULT_ARC_WIDTH = 14;
    private static final double DEFAULT_ARC_HEIGHT = 14;
    public static final Color DEFAULT_COLOR = Color.LIGHTBLUE;
    private static final Duration DEFAULT_SHOOT_COOLDOWN = Duration.ZERO;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        return FXGL.entityBuilder(data).type(EntityType.PADDLE).collidable();
    }

    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        final double width = Utilities.Typing.getOr(data, "width", DEFAULT_WIDTH);
        final double height = Utilities.Typing.getOr(data, "height", DEFAULT_HEIGHT);
        final var color = Utilities.Typing.getOr(data, "color", DEFAULT_COLOR);
        final double arcWidth = Utilities.Typing.getOr(data, "arcWidth", DEFAULT_ARC_WIDTH);
        final double arcHeight = Utilities.Typing.getOr(data, "arcHeight", DEFAULT_ARC_HEIGHT);

        final var view = new Rectangle(width, height, color);
        view.setArcWidth(arcWidth);
        view.setArcHeight(arcHeight);

        return getBuilder(data)
                .viewWithBBox(view)
                .with(new PaddleShooting(DEFAULT_SHOOT_COOLDOWN))
                .build();
        // TODO: Thêm Dashing
    }
}
