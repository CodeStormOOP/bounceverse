package com.github.codestorm.bounceverse.components._old.ball;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;

/**
 *
 *
 * <h1>Ball Component</h1>
 *
 * <p>This component adds movement and interaction behavior to a {@code Ball} entity.
 *
 * <p>By default,
 *
 * <ul>
 *   <li>The ball moves with a velocity of 200.
 *   <li>The ball bounces when it collides with walls.
 * </ul>
 *
 * @author minngoc1213
 */
public class BallComponent extends Component {
    public static final int SPEED = 500;

    private PhysicsComponent physics;
    private Point2D velocity = new Point2D(SPEED, SPEED);

    @Override
    public void onUpdate(double tpf) {
        var v = physics.getLinearVelocity();

        double vx = v.getX();
        double vy = v.getY();

        // avoid ball stick to wall
        if (Math.abs(vx) < 50) {
            vx = (vy >= 0) ? 50 : -50;
        }

        // avoid ball stick to wall
        if (Math.abs(vy) < 50) {
            vy = (vx >= 0) ? 50 : -50;
        }

        Point2D velocity = new Point2D(vx, vy);

        // avoid ball spin
        physics.setLinearVelocity(velocity.normalize().multiply(SPEED));
        physics.setAngularVelocity(0);
        physics.getEntity().setRotation(0);
    }

    @Override
    public void onAdded() {
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(velocity));
    }
}
