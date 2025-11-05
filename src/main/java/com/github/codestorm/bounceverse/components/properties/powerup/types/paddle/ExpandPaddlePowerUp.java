package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleSizeManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;

import javafx.util.Duration;

/**
 * PowerUp mở rộng Paddle tạm thời.
 */
public final class ExpandPaddlePowerUp extends Component {

    private static final Duration DURATION = Duration.seconds(8);

    @Override
    public void onAdded() {
        var paddle = getEntity();
        var manager = paddle.getComponent(PaddleSizeManager.class);
        PowerUpManager.getInstance().activate(
                "ExpandPaddle",
                DURATION,
                () -> manager.expand(1.5), 
                manager::resetSize 
        );
    }
}
