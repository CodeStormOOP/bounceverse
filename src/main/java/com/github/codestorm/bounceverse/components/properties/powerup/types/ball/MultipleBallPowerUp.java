package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;

/**
 * PowerUp nhân đôi toàn bộ bóng hiện có.
 * Mỗi bóng sinh thêm 1 bóng mới tách từ vị trí của nó và bay lệch nhẹ sang 2 hướng.
 */
public final class MultipleBallPowerUp extends PowerUp {

    public MultipleBallPowerUp() {
        super("MultipleBall");
    }

    @Override
    public void apply(Entity paddle) {
        var balls = FXGL.getGameWorld().getEntitiesByType(EntityType.BALL);

        // copy list để tránh ConcurrentModificationException khi spawn trong loop
        var currentBalls = List.copyOf(balls);

        for (Entity ball : currentBalls) {
            ball.getComponentOptional(PhysicsComponent.class).ifPresent(phys -> {
                Point2D pos = ball.getCenter();
                Point2D velocity = phys.getLinearVelocity();

                if (velocity.magnitude() < 1e-3)
                    return;

                // tạo 2 bóng lệch trái/phải
                spawnSplitBall(pos, velocity, 20);
                spawnSplitBall(pos, velocity, -20);
            });
        }
    }

    private void spawnSplitBall(Point2D pos, Point2D velocity, double deg) {
        double angle = Math.toRadians(deg);
        Point2D newVelocity = rotateVector(velocity, angle);

        // spawn tại vị trí bóng cũ, nhưng dịch nhẹ theo hướng mới để tránh overlap
        Point2D spawnPos = pos.add(newVelocity.normalize().multiply(8));

        Entity newBall = FXGL.spawn("ball", spawnPos);

        // ép đặt velocity sau khi spawn (ghi đè mọi mặc định trong BallFactory)
        newBall.getComponentOptional(PhysicsComponent.class).ifPresent(newPhys -> {
            newPhys.setLinearVelocity(newVelocity);

            // đảm bảo ngay frame sau velocity vẫn giữ nguyên
            FXGL.runOnce(() -> newPhys.setLinearVelocity(newVelocity), javafx.util.Duration.millis(20));
        });
    }

    private static Point2D rotateVector(Point2D v, double angleRad) {
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        return new Point2D(
                v.getX() * cos - v.getY() * sin,
                v.getX() * sin + v.getY() * cos
        );
    }
}
