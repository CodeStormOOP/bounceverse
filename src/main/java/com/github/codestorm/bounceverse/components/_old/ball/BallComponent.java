package com.github.codestorm.bounceverse.components.ball;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.factory.BallFactory;
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
    public static final int VELOCITY = 200;

    private Point2D velocity = new Point2D(VELOCITY, VELOCITY);

    @Override
    public void onUpdate(double tpf) {

        entity.translate(velocity.multiply(tpf));

        // bounce if colliding horizontal wall
        if (entity.getY() <= BallFactory.RADIUS
                || entity.getY() >= FXGL.getAppHeight() - BallFactory.RADIUS) {
            velocity = new Point2D(velocity.getX(), -velocity.getY());
        }

        // bounce if colliding vertical wall
        if (entity.getX() <= BallFactory.RADIUS
                || entity.getX() >= FXGL.getAppWidth() - BallFactory.RADIUS) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
        }
    }
}
