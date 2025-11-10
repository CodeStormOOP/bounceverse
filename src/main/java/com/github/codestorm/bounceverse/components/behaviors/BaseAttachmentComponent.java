package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;

/**
 * Cho phép bóng dính paddle và di chuyển cùng paddle.
 */
public class BaseAttachmentComponent extends Component {

    protected Entity paddle;
    protected PhysicsComponent physics;
    protected boolean attached = false;

    private double offsetX;
    private double offsetY;
    private double lastPaddleX;

    @Override
    public void onAdded() {
        paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        physics = getEntity().getComponent(PhysicsComponent.class);

        // FIX: Dừng hoàn toàn chuyển động khi mới spawn
        physics.setLinearVelocity(Point2D.ZERO);
        physics.getBody().setAwake(false); // tắt vật lý để không bị nảy

        // FIX: Cho biết đang gắn paddle
        attached = true;

        lastPaddleX = paddle.getX();
        offsetX = getEntity().getX() - paddle.getX();
        offsetY = getEntity().getY() - paddle.getY();
    }

    @Override
    public void onUpdate(double tpf) {
        if (!attached || paddle == null)
            return;

        double deltaX = paddle.getX() - lastPaddleX;
        lastPaddleX = paddle.getX();

        double newX = getEntity().getX() + deltaX;
        double newY = paddle.getY() + offsetY;

        physics.overwritePosition(new Point2D(newX, newY));
        physics.setLinearVelocity(Point2D.ZERO);

        // FIX: đảm bảo physics không bị wake lại bởi va chạm
        physics.getBody().setAwake(false);
    }

    /** Khi bóng vừa dính paddle – đặt lại offset chuẩn. */
    public void snapToPaddle(Entity ball) {
        if (paddle == null)
            return;

        attached = true; // FIX: gắn lại
        physics.setLinearVelocity(Point2D.ZERO);
        physics.getBody().setAwake(false);

        lastPaddleX = paddle.getX();
        offsetX = ball.getX() - paddle.getX();
        offsetY = ball.getY() - paddle.getY();

        physics.overwritePosition(ball.getPosition());
    }

    /** Thả bóng ra khỏi paddle với góc và tốc độ nhất định. */
    public void releaseBall(double angleDeg, double speed) {
        if (!attached)
            return;

        attached = false;
        physics.getBody().setAwake(true); // kích hoạt lại vật lý

        double angle = Math.toRadians(angleDeg);
        double dir = (getEntity().getCenter().getX() >= paddle.getCenter().getX()) ? 1 : -1;
        double vx = speed * Math.sin(angle) * dir;
        double vy = -speed * Math.cos(angle);

        physics.setLinearVelocity(new Point2D(vx, vy));
    }

    public void setAttached(boolean value) {
        this.attached = value;

        if (physics != null) {
            if (value) {
                physics.setLinearVelocity(Point2D.ZERO);
                physics.getBody().setAwake(false);
            } else {
                physics.getBody().setAwake(true);
            }
        }
    }

    public boolean isAttached() {
        return attached;
    }
}
