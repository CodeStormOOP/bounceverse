package com.github.codestorm.bounceverse.powerup;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * <h1><b>PowerUp</b></h1>
 *
 * PowerUp provide special effects and abilities to playable objects. <i>They are designed to update
 * based on events (lazy-update).</i>
 *
 * <p><i>You need to extend this class to create another class that can provide effects though
 * {@code apply()} and {@code doApply()} method.</i>
 */
public abstract class PowerUp {
    private TimerAction waitAction;
    private TimerAction activeAction;
    private double startAt;
    private double endAt;

    /** Update {@code waitAction} when {@code startAt} is changed. */
    private void updateWaitAction() {
        final var gameTimer = FXGL.getGameTimer();
        final var currentTime = gameTimer.getNow();
        // when not executed
        if (currentTime < startAt) {
            if (waitAction != null) {
                waitAction.expire();
            }
            final var timeLeft = Duration.millis(startAt - currentTime);
            waitAction = gameTimer.runOnceAfter(this::doApply, timeLeft);
        }
    }

    /** Update {@code activeAction} when {@code endAt} is changed. */
    private void updateActiveAction() {
        final var gameTimer = FXGL.getGameTimer();
        final var currentTime = gameTimer.getNow();
        // when not executed
        if (currentTime < endAt) {
            if (activeAction != null) {
                activeAction.expire();
            }
            final var timeLeft = Duration.millis(endAt - currentTime);
            activeAction = gameTimer.runOnceAfter(this::doUnapply, timeLeft);
        }
    }

    /** Wrapper for {@code apply()} method. */
    protected void doApply() {
        apply();
    }

    /** Wrapper for {@code unapply()} method. */
    protected void doUnapply() {
        unapply();
    }

    /**
     * Apply PowerUp effects.
     *
     * <p>Auto use when active power up.
     */
    protected abstract void apply();

    /**
     * Unapply PowerUp effects.
     *
     * <p>Auto use when power up is ended.
     */
    protected abstract void unapply();

    public double getStartAt() {
        return startAt;
    }

    public double getEndAt() {
        return endAt;
    }

    public void setStartAt(double startAt) {
        this.startAt = startAt;
        updateWaitAction();
    }

    public void setEndAt(double endAt) {
        this.endAt = endAt;
        updateActiveAction();
    }

    /**
     * Create PowerUp object.
     *
     * @param startAt Time that PowerUp start
     * @param endAt Time that PowerUp end
     */
    public PowerUp(double startAt, double endAt) {
        setStartAt(startAt);
        setEndAt(endAt);
    }

    /**
     * Create PowerUp object.
     *
     * @param duration Duration
     * @param after Duration to wait before active
     */
    public PowerUp(@NotNull Duration duration, @NotNull Duration after) {
        final var currentTime = FXGL.getGameTimer().getNow();
        this(currentTime + after.toMillis(), currentTime + after.toMillis() + duration.toMillis());
    }

    /**
     * Create PowerUp object and active.
     *
     * @param duration Duration
     */
    public PowerUp(@NotNull Duration duration) {
        this(duration, Duration.ZERO);
    }
}
