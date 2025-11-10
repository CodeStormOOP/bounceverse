package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.github.codestorm.bounceverse.components.behaviors.paddle.MagnetComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.almasb.fxgl.entity.Entity;
import javafx.util.Duration;

/**
 * Power-Up: Paddle có thể "bắt dính" bóng (Magnet effect).
 */
public final class MagnetPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(15);

    public MagnetPowerUp() {
        super("Magnet");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                () -> paddle.addComponent(new MagnetComponent()),
                () -> paddle.removeComponent(MagnetComponent.class));
    }
}
