package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleSizeManager;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleTextureManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import javafx.util.Duration;

/**
 * Power-Up mở rộng Paddle tạm thời.
 */
public final class ExpandPaddlePowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(8);

    public ExpandPaddlePowerUp() {
        super("ExpandPaddle");
    }

    @Override
    public void apply(Entity paddle) {
        var sizeManager = paddle.getComponent(PaddleSizeManager.class);
        var textureManager = paddle.getComponent(PaddleTextureManager.class);

        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                () -> {
                    sizeManager.expand(1.5);
                    textureManager.setExpandState();
                },
                () -> {
                    sizeManager.resetSize();
                    textureManager.setNormalState();
                });
    }
}
