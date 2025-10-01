package com.github.codestorm.bounceverse.paddle;

import java.util.ArrayList;
import java.util.List;

/**
 * A special type of {@link Paddle} that can shoot bullets upward to destroy bricks. This is usually
 * activated by a power-up.
 */
public class LaserPaddle extends Paddle {
    /** List of bullets currently fired by the paddle. */
    private List<LaserPaddle> bullets;

    // Create a new LaserPaddle instance
    public LaserPaddle(int x, int y, int width, int height, double speed) {
        super(x, y, width, height, speed);
        this.bullets = new ArrayList<>();
    }

    // Shoots bullet from paddle.
    public void shoot() {}

    /** Updates all bullets when them get off-screen or hit the brick. */
    public void updateBullets() {}

    public List<LaserPaddle> getBullets() {
        return bullets;
    }
}
