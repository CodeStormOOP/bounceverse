package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.util.Duration;

/**
 * Power-Up làm to bóng trong một thời gian ngắn.
 */
public class ExpandBallPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(8);

    public ExpandBallPowerUp() {
        super("ExpandBall");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                this::expandBalls,
                this::resetBalls
        );
    }

    private void expandBalls() {
        for (Entity ball : FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)) {
            var view = ball.getViewComponent().getChildren().get(0);
            view.setScaleX(1.5);
            view.setScaleY(1.5);
        }
    }

    private void resetBalls() {
        for (Entity ball : FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)) {
            var view = ball.getViewComponent().getChildren().get(0);
            view.setScaleX(1);
            view.setScaleY(1);
        }
    }
}
