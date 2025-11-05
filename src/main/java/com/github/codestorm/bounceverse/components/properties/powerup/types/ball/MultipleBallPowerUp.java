package com.github.codestorm.bounceverse.components.properties.powerup.types.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
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
        var world = FXGL.getGameWorld();
        var balls = world.getEntitiesByType(EntityType.BALL);

        for (Entity ball : balls) {
            var physicsOpt = ball.getComponentOptional(PhysicsComponent.class);
            if (physicsOpt.isEmpty())
                continue;

            var physics = physicsOpt.get();
            Point2D pos = ball.getCenter();
            Point2D velocity = physics.getLinearVelocity();

            // Sinh bóng mới gần vị trí bóng gốc
            Entity newBall = FXGL.spawn("ball", pos.add(10, 0));

            // Đặt vận tốc lệch góc 30 độ để tách quỹ đạo
            Point2D rotatedVelocity = rotateVector(velocity, Math.toRadians(30));
            newBall.getComponent(PhysicsComponent.class).setLinearVelocity(rotatedVelocity);
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
