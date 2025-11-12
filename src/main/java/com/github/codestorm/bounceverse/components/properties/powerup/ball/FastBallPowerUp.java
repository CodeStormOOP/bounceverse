package com.github.codestorm.bounceverse.components.properties.powerup.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.systems.init.GameSystem;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.util.Duration;

/** Làm nhanh bóng trong một khoảng thời gian. */
public class FastBallPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(6);
    private static final double SPEED_MULTIPLIER = 1.5;

    public FastBallPowerUp() {
        super("FastBall");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().activate(name, DURATION, this::speedUpBalls, this::resetBalls);
    }

    private void speedUpBalls() {
        var currentSpeed = FXGL.getd("ballSpeed");
        FXGL.set("ballSpeed", currentSpeed * SPEED_MULTIPLIER);
        updateAllBallSpeeds();
    }

    private void resetBalls() {
        FXGL.set("ballSpeed", GameSystem.Variables.DEFAULT_BALL_SPEED);
        updateAllBallSpeeds();
    }

    private void updateAllBallSpeeds() {
        var newSpeed = FXGL.getd("ballSpeed");
        FXGL.getGameWorld()
                .getEntitiesByType(EntityType.BALL)
                .forEach(
                        ball -> {
                            var physics = ball.getComponent(PhysicsComponent.class);
                            physics.setLinearVelocity(
                                    physics.getLinearVelocity().normalize().multiply(newSpeed));
                        });
    }
}
