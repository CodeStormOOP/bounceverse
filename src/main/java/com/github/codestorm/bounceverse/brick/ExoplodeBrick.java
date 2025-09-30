package com.github.codestorm.bounceverse.brick;

public class ExoplodeBrick extends Brick {
    private static final int explodeRadius = 1;

    public ExoplodeBrick(int x, int y, int width, int height, int hp) {
        super(x, y, width, height, hp);
    }

    public int getRadius() {
        return explodeRadius;
    }

    /**
     * Handles a hit on this brick.
     *
     * <p>Decreases hit points using the parent logic. If the brick is destroyed after the hit, it
     * triggers an explosion.
     */
    @Override
    public void hit() {
        super.hit();
        if (isDestroyed()) {
            explode();
        }
    }

    /**
     * Triggers the explosion effect of this brick.
     *
     * <p>This method can be extended to apply damage to surrounding bricks
     */
    private void explode() {}
}
