package com.github.codestorm.bounceverse.brick;

import javafx.scene.paint.Color;

public class ProtectedBrick extends BrickComponent {
    private String shieldSide;

    public ProtectedBrick(int width, int height, int hp, Color baseColor, String shieldSide) {
        super(width, height, hp, baseColor);
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
    @Override
    public int getScore() {
        return super.getScore() + 20;
    }
}
