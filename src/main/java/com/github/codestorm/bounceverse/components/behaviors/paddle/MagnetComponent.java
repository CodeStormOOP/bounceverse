package com.github.codestorm.bounceverse.components.behaviors.paddle;

import java.util.Optional;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.behaviors.BaseAttachmentComponent;

/**
 * Power-Up Magnet: Paddle bắt bóng và giữ theo paddle.
 */
public final class MagnetComponent extends BaseAttachmentComponent {

    private Optional<Entity> attachedBall = Optional.empty();
    private boolean waitingToRelease = false;

    /** Khi paddle chạm bóng – thử bắt bóng lại. */
    public void tryAttachBall(Entity ball) {
        if (attachedBall.isPresent())
            return;

        attachedBall = Optional.of(ball);

        ball.getComponentOptional(BaseAttachmentComponent.class)
            .ifPresent(ballAttach -> {
                ballAttach.setAttached(true);
                ballAttach.snapToPaddle(ball); // gắn đúng vị trí va chạm
            });

        waitingToRelease = true;
    }

    /** Thả bóng ra khi người chơi nhấn SPACE. */
    public void releaseBallExternal() {
        if (attachedBall.isEmpty())
            return;

        var ball = attachedBall.get();
        ball.getComponentOptional(BaseAttachmentComponent.class)
            .ifPresent(c -> c.releaseBall(45, 300)); 

        attachedBall = Optional.empty();
        waitingToRelease = false;
    }

    public boolean hasBallAttached() {
        return attachedBall.isPresent() && waitingToRelease;
    }
}
