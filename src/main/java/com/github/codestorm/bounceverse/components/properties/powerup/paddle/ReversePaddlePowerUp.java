package com.github.codestorm.bounceverse.components.properties.powerup.paddle;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.behaviors.paddle.ReverseControlComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;

import javafx.util.Duration;

/** Power-Up: Đảo ngược điều khiển của paddle trong thời gian ngắn. */
public final class ReversePaddlePowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(10);

    public ReversePaddlePowerUp() {
        super("ReversePaddle");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance()
                .activate(
                        name,
                        DURATION,
                        () -> {
                            // Nếu paddle chưa có component thì thêm
                            if (!paddle.hasComponent(ReverseControlComponent.class)) {
                                paddle.addComponent(new ReverseControlComponent());
                            }
                        },
                        () -> {
                            // Khi hết hạn thì gỡ component nếu còn
                            if (paddle.hasComponent(ReverseControlComponent.class)) {
                                paddle.removeComponent(ReverseControlComponent.class);
                            }
                        });
    }
}
