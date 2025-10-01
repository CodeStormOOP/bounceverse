package com.github.codestorm.bounceverse.paddle;

/**
 *
 *
 * <h1><b>LaserPaddle</b></h1>
 *
 * A special type of {@link Paddle} that can shoot bullets upward to destroy bricks. This is usually
 * activated by a {@link com.github.codestorm.bounceverse.powerup.PowerUp} (not available).
 */
public class LaserPaddle extends Paddle {
    /** Create a new LaserPaddle instance. */
    public LaserPaddle(int x, int y, int width, int height, double speed) {
        super(x, y, width, height, speed);
    }

    /** Shoots bullet from paddle. */
    public void shoot() {}
}
