package com.github.codestorm.bounceverse.components.properties.powerup.types;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.util.Duration;

/**
 * PowerUp thu nhỏ Paddle tạm thời.
 */
public final class ShrinkPaddlePowerUp extends Component {

    private static final double SCALE_FACTOR = 0.7; // thu nhỏ còn 70%
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

        // thu nhỏ paddle
        view.setScaleX(originalScale * SCALE_FACTOR);

        // sau 5 giây phục hồi
        FXGL.getGameTimer().runOnceAfter(() -> {
            view.setScaleX(originalScale);
        }, DURATION);
    }
}
