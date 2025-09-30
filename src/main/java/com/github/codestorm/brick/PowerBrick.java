package com.github.codestorm.brick;

public class PowerBrick extends Brick {
    public PowerBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, hp);
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
