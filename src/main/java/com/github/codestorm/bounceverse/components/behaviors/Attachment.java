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

    @Override
    public void onAdded() {
        paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        physics = getEntity().getComponent(PhysicsComponent.class);
    }

    @Override
    public void onUpdate(double tpf) {
        if (attached && paddle != null) {
            double paddleCenterX = paddle.getCenter().getX();
            double paddleTopY = paddle.getY();

            // canh giữa theo tâm paddle
            double x = paddleCenterX - entity.getWidth() / 2 + 10;
            // đặt bóng ngay trên mặt paddle, cách 1 px cho đẹp
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

        // Đánh thức body và cho vận tốc bay lên
        physics.getBody().setAwake(true);
        physics.setLinearVelocity(new Point2D(0, -300));
    }

    public boolean isAttached() {
        return attached;
    }
}
