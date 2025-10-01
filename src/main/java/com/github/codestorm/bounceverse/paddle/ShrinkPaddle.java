package com.github.codestorm.bounceverse.paddle;

/** A special type of {@link Paddle} that is narrower than the normal paddle. */
public class ShrinkPaddle extends Paddle {
    // The scale factor applied to the paddle's width.
    private static final double SCALE = 0.7;

    // Create a new shrunk paddle.
    public ShrinkPaddle(int x, int y, int width, int height, double speed) {
        super(x, y, (int) (width * SCALE), height, speed);
    }
}
