package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;

/**
 * PowerUp nhân đôi toàn bộ bóng hiện có.
 * Mỗi bóng sinh thêm 1 bóng mới lệch góc nhẹ để bay tách ra.
 */
public final class MultipleBallPowerUp extends PowerUp {

    public MultipleBallPowerUp() {
        super("MultipleBall");
    }

    @Override
    public void apply(Entity paddle) {
        var balls = FXGL.getGameWorld().getEntitiesByType(EntityType.BALL);

        for (Entity ball : balls) {
            ball.getComponentOptional(PhysicsComponent.class).ifPresent(physics -> {
                Point2D pos = ball.getCenter();
                Point2D velocity = physics.getLinearVelocity();

                // Offset nhẹ theo hướng bay hiện tại
                Point2D spawnPos = pos.add(velocity.normalize().multiply(15));

                Entity newBall = FXGL.spawn("ball", spawnPos);

                // Lệch góc ±30 độ
                double angle = Math.toRadians(FXGL.random(-30, 30));
                Point2D rotatedVelocity = rotateVector(velocity, angle);

                newBall.getComponent(PhysicsComponent.class).setLinearVelocity(rotatedVelocity);
            });
        }
    }

    /** Xoay vector một góc nhất định (đơn vị radian). */
    private static Point2D rotateVector(Point2D v, double angleRad) {
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        return new Point2D(
                v.getX() * cos - v.getY() * sin,
                v.getX() * sin + v.getY() * cos);
    }
}
