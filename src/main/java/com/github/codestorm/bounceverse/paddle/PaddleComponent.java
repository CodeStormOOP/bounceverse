package com.github.codestorm.bounceverse.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

/**
 * Represents the paddle controlled by the player in the game.
 *
 * <p>The paddle can move horizontally, reset its position, and is the base class for other paddle
 * variants (expand, shrink, laser, etc.).
 */
public class PaddleComponent extends Component {
    private double speed;

    public PaddleComponent(double speed) {
        this.speed = speed;
    }

    // getter & setter.
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Moves the paddle to the left based on its speed and the current frame time.
     *
     * <p>Using {@code FXGL.tpf()} (Time Per Frame) ensures that movement is frame rate–independent,
     * so the paddle moves smoothly even if the FPS varies.
     */
    public void moveLeft() {
        double newX = entity.getX() - speed * FXGL.tpf();
        if (newX >= 0) {
            entity.setX(newX);
        }
    }

    /**
     * Moves the paddle to the right based on its speed and the current frame time.
     *
     * <p>Using {@code FXGL.tpf()} (Time Per Frame) ensures that movement is frame rate–independent,
     * so the paddle moves smoothly even if the FPS varies.
     */
    public void moveRight() {
        double newX = entity.getX() + speed * FXGL.tpf();
        double maxX = FXGL.getAppWidth() - entity.getWidth();
        if (newX <= maxX) {
            entity.setX(newX);
        }
    }

    // Reset the paddle to a specific position.
    public void resetPosition(double startX, double startY) {
        entity.setX(startX);
        entity.setY(startY);
    }
}
