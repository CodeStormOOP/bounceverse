package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;

public class Attachment extends Component {

    private Entity paddle;
    private boolean attached = true;
    private PhysicsComponent physics;

    private static boolean move = false;
    private double moveSpeed = 50;
    private double direction = 1;
    private double maxOffset = 50;
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
            double paddleCenterX = paddle.getCenter().getX();
            double paddleTopY = paddle.getY();

            double deltaX = paddle.getX() - lastPaddleX;
            lastPaddleX = paddle.getX();

            if (!move && Math.abs(deltaX) > 0.5) {
                move = true;
                direction = Math.signum(deltaX);
            }

            if (move) {
                currentOffset += direction * moveSpeed * tpf;

                if (Math.abs(currentOffset) > maxOffset) {
                    direction *= -1;
                }
            }

            // tính vị trí mới
            double x = paddleCenterX - entity.getWidth() / 2 + currentOffset + 10;
            double y = paddleTopY - BallFactory.DEFAULT_RADIUS * 2 + 5;

            entity.setPosition(x, y);
            physics.setLinearVelocity(Point2D.ZERO);
        }
    }

    public void releaseBall() {
        if (!attached) {
            return;
        }

        attached = false;
        physics.overwritePosition(entity.getPosition());
        physics.getBody().setAwake(true);

        double ballCenterX = entity.getCenter().getX();
        double paddleCenterX = paddle.getCenter().getX();

        double dir = (ballCenterX >= paddleCenterX) ? 1 : -1;

        double angle = Math.toRadians(45);
        double speed = 350;

        // Tính vận tốc thành phần
        double vx = speed * Math.sin(angle) * dir;
        double vy = -speed * Math.cos(angle);

        physics.setLinearVelocity(new Point2D(vx, vy));

        move = false;
    }

    public boolean isAttached() {
        return attached;
    }

    public static boolean isMove() {
        return move;
    }

    public static void setMove(boolean value) {
        move = value;
    }
}
