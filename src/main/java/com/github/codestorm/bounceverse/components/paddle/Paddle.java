package com.github.codestorm.bounceverse.components.paddle;

/**
 * Represents the paddle controlled by the player in the game.
 *
 * <p>The paddle can move horizontally, reset its position, and is the base class for other paddle
 * variants (expand, shrink, laser, etc.).
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

    // getter & setter.
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

    // Move the paddle left.
    public void moveLeft() {}

    // Move the paddle right.
    public void moveRight() {}

    // Reset the paddle to a specific position.
    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }
}
