package com.github.codestorm.bounceverse.components.properties.powerup.paddle;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleViewManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;

import javafx.util.Duration;

/** Power-Up mở rộng Paddle tạm thời. */
public final class ExpandPaddlePowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(8);

    public ExpandPaddlePowerUp() {
        super("ExpandPaddle");
    }

    @Override
    public void apply(Entity paddle) {
        var viewManager = paddle.getComponent(PaddleViewManager.class);

        PowerUpManager.getInstance()
                .activate(name, DURATION, viewManager::setExpandState, viewManager::setNormalState);
    }
}
