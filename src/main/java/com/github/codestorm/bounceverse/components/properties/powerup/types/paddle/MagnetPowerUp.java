package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.behaviors.paddle.MagnetComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;

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
        if (paddle.getComponentOptional(MagnetComponent.class).isEmpty()) {
            paddle.addComponent(new MagnetComponent());
        }

        // Sau DURATION thì remove component
        FXGL.runOnce(() -> {
            paddle.removeComponent(MagnetComponent.class);
        }, DURATION);
    }
}
