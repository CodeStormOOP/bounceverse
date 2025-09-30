package com.github.codestorm.ball;

/**
 * Abstract representation of a ball. A ball has position (x, y), velocity (vx, vy), and a radius.
 * Subclasses must implement movement and bounce behaviors.
 *
 * <p>This class provides utility methods for resetting position, setting velocity, checking bounds,
 * and accessing attributes.
 *
 * @author minngoc1213
 */
public abstract class Ball {
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double radius;

    /** Default constructor. */
    public Ball() {}

    /** Constructor with full parameters. */
    public Ball(double x, double y, double vx, double vy, double radius) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
    }

    /** Draws the ball on the screen. */
    public void draw() {}

    /**
     * Resets the ball to a new position and velocity.
     *
     * @param startX new X coordinate
     * @param startY new Y coordinate
     * @param startVx new horizontal velocity
     * @param startVy new vertical velocity
     */
    public void reset(double startX, double startY, double startVx, double startVy) {
        this.x = startX;
        this.y = startY;
        this.vx = startVx;
        this.vy = startVy;
    }

    /** Updates the position of the ball according to its velocity. */
    public abstract void move();

    /** Handles behavior when the ball bounces against a horizontal wall. */
    public abstract void bounceHorizontal();

    /** Handles behavior when the ball bounces against a vertical wall. */
    public abstract void bounceVertical();

    /**
     * Sets the velocity of the ball.
     *
     * @param vx horizontal velocity
     * @param vy vertical velocity
     */
    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    /**
     * Checks whether the ball is out of bounds.
     *
     * @return {@code true} if the ball is outside the allowed boundary, {@code false} otherwise
     */
    public boolean isOutOfBounds() {
        return true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
