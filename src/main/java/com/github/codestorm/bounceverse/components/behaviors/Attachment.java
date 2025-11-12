package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;

/** Gắn bóng vào paddle và điều khiển khi người chơi chưa bắn ra. */
public class Attachment extends Component {

    private Entity paddle;
    private boolean attached = true;
    private PhysicsComponent physics;

    private boolean move = false;
    private double direction = 1;
    private double currentOffset = 0;
    private double lastPaddleX;

    @Override
    public void onAdded() {
        paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        physics = getEntity().getComponent(PhysicsComponent.class);
        lastPaddleX = paddle.getX();
    }

    @Override
    public void onUpdate(double tpf) {
        if (attached && paddle != null) {
            var paddleCenterX = paddle.getCenter().getX();
            var paddleTopY = paddle.getY();

            var deltaX = paddle.getX() - lastPaddleX;
            lastPaddleX = paddle.getX();

            if (!move && Math.abs(deltaX) > 0.5) {
                move = true;
                direction = Math.signum(deltaX);
            }

            if (move) {
                double moveSpeed = 50;
                currentOffset += direction * moveSpeed * tpf;
                double maxOffset = 50;
                if (Math.abs(currentOffset) > maxOffset) {
                    direction *= -1;
                }
            }

            var x = paddleCenterX - entity.getWidth() / 2 + currentOffset + 10;
            var y = paddleTopY - BallFactory.DEFAULT_RADIUS * 2 + 5;

            entity.setPosition(x, y);
            physics.setLinearVelocity(Point2D.ZERO);
        }
    }

    public void releaseBall() {
        if (!attached) return;

        attached = false;
        physics.overwritePosition(entity.getPosition());
        physics.getBody().setAwake(true);

        double speed = 350;
        double vx = 0;
        var vy = -speed;

        physics.setLinearVelocity(new Point2D(vx, vy));
        move = false;
    }

    public boolean isAttached() {
        return attached;
    }
}
