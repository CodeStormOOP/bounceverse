package com.github.codestorm.bounceverse.components._old.brick;

import javafx.scene.paint.Color;

public class PowerBrick extends BrickComponent {

    public PowerBrick(int width, int height, int hp, Color baseColor) {
        super(width, height, hp, baseColor);
    }

    /**
     * Handles a hit on this power brick.
     *
     * <p>Executes the normal hit logic from the parent class. If the brick is destroyed after the
     * hit, it will randomly spawn its power-up.
     */
    @Override
    public void hit() {
        super.hit();
        if (isDestroyed()) {
            activePower();
        }
    }

    /**
     * Spawns the power-up associated with this brick.
     *
     * <p>Subclasses should override this method to define the specific power-up effect.
     */
    public void activePower() {}
}
