package com.github.codestorm.bounceverse.components.properties.powerup.types;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.util.Duration;

/**
 * PowerUp mở rộng Paddle tạm thời.
 */
public final class ExpandPaddlePowerUp extends Component {

    private static final double SCALE_FACTOR = 1.5; // mở rộng 150%
    private static final Duration DURATION = Duration.seconds(5);

    @Override
    public void onAdded() {
        var paddle = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PADDLE)
                .stream()
                .findFirst()
                .orElse(null);

        if (paddle == null) return;

        var view = paddle.getViewComponent().getParent();
        var originalScale = view.getScaleX();

        // mở rộng paddle
        view.setScaleX(originalScale * SCALE_FACTOR);

        // sau 5 giây thu nhỏ lại
        FXGL.getGameTimer().runOnceAfter(() -> {
            view.setScaleX(originalScale);
        }, DURATION);
    }
}
