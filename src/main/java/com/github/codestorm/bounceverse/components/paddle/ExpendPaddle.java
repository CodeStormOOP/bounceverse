package com.github.codestorm.bounceverse.components.paddle;

/** A special type of {@link Paddle} that is wider than the normal paddle. */
public class ExpendPaddle extends Paddle {
    // The scale factor applied to the paddle's width.
    private static final double SCALE = 1.5;

    // Create a new expended paddle.
    public ExpendPaddle(int x, int y, int width, int height, double speed) {
        super(x, y, (int) (width * SCALE), height, speed);
    }
}
