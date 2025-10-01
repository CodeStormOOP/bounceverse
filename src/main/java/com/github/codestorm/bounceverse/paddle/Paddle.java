package com.github.codestorm.bounceverse.paddle;

/**
 *
 *
 * <h1><b>Paddle</b></h1>
 *
 * {@link Paddle} is an object directly controlled by the player, used to interact with {@link
 * com.github.codestorm.bounceverse.ball.Ball}.
 *
 * <p>The paddle can move horizontally and reset its position.
 */
public class Paddle {
    private int x;
    private int y;
    private int width;
    private int height;
    private double speed;

    public Paddle(int x, int y, int width, int height, double speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /** Move Paddle to left. */
    public void moveLeft() {}

    /** Move Paddle to right. */
    public void moveRight() {}

    /** Reset paddle to a specific position. */
    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }
}
