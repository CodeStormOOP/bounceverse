package com.github.codestorm.bounceverse.paddle;

/** A special type of {@link Paddle} that is narrower than the normal paddle. */
public class ShrinkPaddle extends PaddleComponent {

    // Constructor.
    public ShrinkPaddle(double speed) {
        super(speed);
    }

    @Override
    public void onAdded() {
        entity.setScaleX(0.7);
    }

    public void resetSize() {
        entity.setScaleX(1.0);
    }
}
