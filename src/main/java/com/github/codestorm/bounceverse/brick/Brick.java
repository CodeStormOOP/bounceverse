package com.github.codestorm.bounceverse.brick;

public class Brick {
    private int x, y, width, height;
    private int hp;
    private final int initialHp;
    private boolean destroyed;

    public Brick(int x, int y, int width, int height, int hp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.initialHp = hp;
        this.destroyed = false;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * Reduces the brick's hit points by one when hit.
     *
     * <p>If hit points reach zero, the brick is marked as destroyed.
     */
    public void hit() {
        if (!destroyed && hp > 0) {
            hp--;
            if (hp == 0) {
                this.destroyed = true;
            }
        }
    }

    // Calculates the score awarded for destroying this brick.
    public int getScore() {
        return initialHp * 10;
    }
}
