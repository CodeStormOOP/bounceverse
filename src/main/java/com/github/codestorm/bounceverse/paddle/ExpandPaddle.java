package com.github.codestorm.bounceverse.paddle;

/** A special type of {@link Paddle} that is wider than the normal paddle. */
public class ExpandPaddle extends PaddleComponent {
    // constructor.
    public ExpandPaddle(double speed) {
        super(speed);
    }

    @Override
    public void onAdded() {
        entity.setScaleX(1.5);
    }

    public void resetSize() {
        entity.setScaleX(1.0);
    }
}
