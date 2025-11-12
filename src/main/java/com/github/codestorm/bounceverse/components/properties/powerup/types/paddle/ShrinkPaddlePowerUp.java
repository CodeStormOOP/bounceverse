package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleSizeManager;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleTextureManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import javafx.util.Duration;

/**
 * Power-Up thu nhỏ Paddle tạm thời.
 */
public final class ShrinkPaddlePowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(8);

    public ShrinkPaddlePowerUp() {
        super("ShrinkPaddle");
    }

    @Override
    public void apply(Entity paddle) {
        var manager = paddle.getComponent(PaddleSizeManager.class);
        var textureManager = paddle.getComponent(PaddleTextureManager.class);

        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                () -> {
                    manager.shrink(0.7);
                    textureManager.setShrinkState();
                },
                () -> {
                    manager.resetSize();
                    textureManager.setNormalState();
                });
    }
}
