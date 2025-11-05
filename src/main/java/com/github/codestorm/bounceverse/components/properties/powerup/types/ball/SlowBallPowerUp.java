package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.util.Duration;

/**
 * Làm chậm tốc độ tất cả bóng tạm thời.
 */
public class SlowBallPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(6);

    public SlowBallPowerUp() {
        super("SlowBall");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                this::slowBalls,
                this::resetBalls
        );
    }

    private void slowBalls() {
        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)
            .forEach(ball -> {
                var physics = ball.getComponent(PhysicsComponent.class);
                physics.setLinearVelocity(physics.getLinearVelocity().multiply(0.7));
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
