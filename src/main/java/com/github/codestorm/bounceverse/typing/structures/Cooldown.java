package com.github.codestorm.bounceverse.typing.structures;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;

import javafx.util.Duration;

/**
 *
 *
 * <h1>{@link Cooldown}</h1>
 *
 * Thời gian hồi để thực hiện lại gì đó. Cooldown hiện tại được lưu trong {@link #current}.
 *
 * @see ActiveCooldown
 */
public class Cooldown {
    protected final ActiveCooldown current = new ActiveCooldown();
    protected Duration duration = Duration.INDEFINITE;

    public Duration getDuration() {
        return duration;
    }

    /**
     * Set thời lượng cooldown. <br>
     * <b>Lưu ý: Chỉ áp dụng cho cooldown mới.</b>
     *
     * @param duration Thời lượng mới
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ActiveCooldown getCurrent() {
        return current;
    }

    public Cooldown() {}

    public Cooldown(Duration duration) {
        this.duration = duration;
    }

    /** Cooldown thời điểm hiện tại. Giống như một wrapper của {@link TimerAction}. */
    public class ActiveCooldown {
        protected TimerAction waiter = null;
        protected double timestamp = Double.NaN;
        protected Runnable onExpiredCallback = null;

        /** Hành động khi cooldown hết. */
        protected void onExpired() {
            timestamp = Double.NaN;
            if (onExpiredCallback != null) {
                onExpiredCallback.run();
            }
        }

        /**
         * Callback thực thi khi cooldown hết hạn.
         *
         * @param callback Callback sẽ thực thi
         */
        public void setOnExpired(Runnable callback) {
            this.onExpiredCallback = callback;
        }

        /**
         * Kiểm tra Cooldown hiện tại hết hạn chưa.
         *
         * @return {@code true} nếu hết hạn, ngược lại {@code false}.
         */
        public boolean isExpired() {
            return (waiter == null) || waiter.isExpired();
        }

        /** Khiến cooldown hết hạn ngay (nếu có). */
        public void expire() {
            if (!isExpired()) {
                waiter.expire();
            }
        }

        /** Set một cooldown mới. */
        public void createNew() {
            expire();

            final var gameTimer = FXGL.getGameTimer();
            waiter = gameTimer.runOnceAfter(this::onExpired, duration);
            timestamp = gameTimer.getNow();
        }

        /** Tạm dừng cooldown. */
        public void pause() {
            if (!isExpired()) {
                waiter.pause();
            }
        }

        /** Tiếp tục cooldown. */
        public void resume() {
            if (!isExpired()) {
                waiter.resume();
            }
        }

        public boolean isPaused() {
            return !isExpired() && waiter.isPaused();
        }

        /**
         * Lấy thời gian còn lại của cooldown.
         *
         * @return Thời gian còn lại
         */
        public Duration getTimeLeft() {
            if (isExpired()) {
                return Duration.ZERO;
            }
            final var elapsed = Duration.millis(FXGL.getGameTimer().getNow() - timestamp);
            return duration.subtract(elapsed);
        }

        /**
         * Giảm thời gian hồi đi một lượng thời gian.
         *
         * @param duration Thời lượng giảm.
         */
        public void reduce(Duration duration) {
            if (!isExpired()) {
                waiter.update(duration.toMillis());
            }
        }

        protected ActiveCooldown() {}
    }
}
