package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.util.Duration;

/**
 * Làm nhanh bóng trong một khoảng thời gian.
 */
public class FastBallPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(6);

    public FastBallPowerUp() {
        super("FastBall");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                this::speedUpBalls,
                this::resetBalls
        );
    }

    private void speedUpBalls() {
        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)
            .forEach(ball -> {
                var physics = ball.getComponent(PhysicsComponent.class);
                physics.setLinearVelocity(physics.getLinearVelocity().multiply(1.5));
            });
    }

    private void resetBalls() {
        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)
            .forEach(ball -> {
                var physics = ball.getComponent(PhysicsComponent.class);
                physics.setLinearVelocity(physics.getLinearVelocity().normalize().multiply(300));
            });
    }
}
