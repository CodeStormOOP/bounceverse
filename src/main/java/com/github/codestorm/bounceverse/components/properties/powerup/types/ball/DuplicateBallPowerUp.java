package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.util.Duration;
import javafx.geometry.Point2D;

/**
 * PowerUp nhân đôi bóng hiện có.
 * Nếu có nhiều bóng, mỗi bóng sẽ sinh thêm 1 bóng phụ cùng hướng.
 */
public class DuplicateBallPowerUp extends com.almasb.fxgl.entity.component.Component {

    private static final Duration DURATION = Duration.seconds(0.1); 

    @Override
    public void onAdded() {
        PowerUpManager.getInstance().activate(
                "DuplicateBall",
                DURATION,
                this::duplicateBalls,
                () -> {}
        );
    }

    private void duplicateBalls() {
        var world = FXGL.getGameWorld();
        var balls = world.getEntitiesByType(EntityType.BALL);

        for (Entity ball : balls) {
            // Lấy vị trí và vận tốc hiện tại của bóng
            var physics = ball.getComponentOptional(com.almasb.fxgl.physics.PhysicsComponent.class);
            if (physics.isEmpty()) continue;

            Point2D velocity = physics.get().getLinearVelocity();
            Point2D pos = ball.getCenter();

            // Spawn bóng mới
            Entity newBall = FXGL.spawn("ball", pos);
            newBall.getComponent(com.almasb.fxgl.physics.PhysicsComponent.class)
                    .setLinearVelocity(velocity.multiply(-1)); 
        }
    }
}
