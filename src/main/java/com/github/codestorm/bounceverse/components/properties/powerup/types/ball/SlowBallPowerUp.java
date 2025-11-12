package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.systems.init.GameSystem;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.util.Duration;

/**
 * Làm chậm tốc độ tất cả bóng tạm thời.
 */
public class SlowBallPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(6);
    private static final double SPEED_MULTIPLIER = 0.7;

    public SlowBallPowerUp() {
        super("SlowBall");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                this::slowBalls,
                this::resetBalls);
    }

    private void slowBalls() {
        double currentSpeed = FXGL.getd("ballSpeed");
        FXGL.set("ballSpeed", currentSpeed * SPEED_MULTIPLIER);
        updateAllBallSpeeds();
    }

    private void resetBalls() {
        FXGL.set("ballSpeed", GameSystem.Variables.DEFAULT_BALL_SPEED);
        updateAllBallSpeeds();
    }

    private void updateAllBallSpeeds() {
        double newSpeed = FXGL.getd("ballSpeed");
        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)
                .forEach(ball -> {
                    var physics = ball.getComponent(PhysicsComponent.class);
                    physics.setLinearVelocity(physics.getLinearVelocity().normalize().multiply(newSpeed));
                });
    }
}
