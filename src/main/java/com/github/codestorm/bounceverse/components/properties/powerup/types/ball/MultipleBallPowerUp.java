package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;

/**
 * PowerUp nhân đôi toàn bộ bóng hiện có.
 * Mỗi bóng sinh thêm 1 bóng mới tách từ vị trí của nó và bay lệch nhẹ sang 2
 * hướng.
 */
public final class MultipleBallPowerUp extends PowerUp {

    public MultipleBallPowerUp() {
        super("MultipleBall");
    }

    @Override
    public void apply(Entity paddle) {
        var balls = FXGL.getGameWorld().getEntitiesByType(EntityType.BALL);
        var currentBalls = List.copyOf(balls);

        for (Entity ball : currentBalls) {
            ball.getComponentOptional(PhysicsComponent.class).ifPresent(phys -> {
                Point2D pos = ball.getCenter();
                Point2D velocity = phys.getLinearVelocity();

                if (velocity.magnitude() < 1e-3)
                    return;

                // chỉ nhân đôi: mỗi bóng cũ sinh thêm 1 bản sao duy nhất
                spawnDuplicateBall(pos, velocity);
            });
        }
    }

    private void spawnDuplicateBall(Point2D pos, Point2D velocity) {
        // lệch nhẹ góc bay để tránh trùng hướng
        Point2D newVelocity = rotateVector(velocity, 15);

        // spawn tại vị trí bóng gốc (dịch nhẹ 1 chút)
        Point2D spawnPos = pos.add(newVelocity.normalize().multiply(8));

        Entity newBall = FXGL.spawn("ball", new SpawnData(spawnPos.getX(), spawnPos.getY()));

        newBall.getComponentOptional(PhysicsComponent.class).ifPresent(newPhys -> {
            newPhys.setLinearVelocity(newVelocity);

            // giữ velocity ổn định sau 1 frame
            FXGL.runOnce(() -> newPhys.setLinearVelocity(newVelocity), javafx.util.Duration.millis(20));
        });
    }

    private static Point2D rotateVector(Point2D v, double deg) {
        double rad = Math.toRadians(deg);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Point2D(
                v.getX() * cos - v.getY() * sin,
                v.getX() * sin + v.getY() * cos);
    }
}
