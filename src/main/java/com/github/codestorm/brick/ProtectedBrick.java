package com.github.codestorm.brick;

public class ProtectedBrick extends Brick {
    private String shieldSide;

    public ProtectedBrick(int x, int y, int width, int height, int hp, String shieldSide) {
        super(x, y, width, height, hp);
        this.shieldSide = shieldSide;
    }

    public String getShieldSide() {
        return shieldSide;
    }

    public void setShieldSide(String shieldSide) {
        this.shieldSide = shieldSide;
    }

    /** Handles a hit on this protected brick from a given direction. */
    public void hit(String direction) {
        if (!isDestroyed()) {
            if (!direction.equalsIgnoreCase(shieldSide)) {
                super.hit();
            }
        }
    }

    // Score from protected brick have more than 20 points.
    public int getScore() {
        return super.getScore() + 20;
    }
}
