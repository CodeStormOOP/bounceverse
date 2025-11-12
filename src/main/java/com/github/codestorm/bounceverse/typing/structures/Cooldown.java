package com.github.codestorm.bounceverse.typing.structures;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import javafx.util.Duration;

/**
 * Lớp Cooldown đã được sửa lỗi dứt điểm. Logic tính toán thời gian còn lại đã
 * được chuyển ra đúng chỗ và hoạt động chính xác.
 */
public class Cooldown {

    protected final ActiveCooldown current = new ActiveCooldown();
    protected Duration duration = Duration.INDEFINITE;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ActiveCooldown getCurrent() {
        return current;
    }

    public Duration getTimeLeft() {
        if (current.isExpired()) {
            return Duration.ZERO;
        }

        // Tính thời gian đã trôi qua kể từ khi cooldown bắt đầu
        final var elapsed = Duration.millis(FXGL.getGameTimer().getNow() - current.timestamp);

        // Lấy tổng thời gian trừ đi thời gian đã trôi qua
        Duration timeLeft = duration.subtract(elapsed);

        // Đảm bảo không bao giờ trả về giá trị âm
        return timeLeft.lessThan(Duration.ZERO) ? Duration.ZERO : timeLeft;
    }

    public Cooldown() {
    }

    public Cooldown(Duration duration) {
        this.duration = duration;
    }

    // Lớp con ActiveCooldown giờ chỉ tập trung quản lý trạng thái (start, stop, pause)
    public class ActiveCooldown {

        protected TimerAction waiter = null;
        protected double timestamp = Double.NaN; // Thời điểm cooldown bắt đầu
        protected Runnable onExpiredCallback = null;

        protected void onExpired() {
            timestamp = Double.NaN;
            if (onExpiredCallback != null) {
                onExpiredCallback.run();
            }
        }

        public void setOnExpired(Runnable callback) {
            this.onExpiredCallback = callback;
        }

        public boolean isExpired() {
            return (waiter == null) || waiter.isExpired();
        }

        public void expire() {
            if (!isExpired()) {
                waiter.expire();
            }
        }

        public void createNew() {
            if (waiter != null && !waiter.isExpired()) {
                waiter.expire();
            }

            var gameTimer = FXGL.getGameTimer();
            waiter = gameTimer.runOnceAfter(() -> {
                timestamp = Double.NaN;
            }, duration);

            timestamp = gameTimer.getNow();
        }

        public void pause() {
            if (!isExpired()) {
                waiter.pause();
            }
        }

        public void resume() {
            if (!isExpired()) {
                waiter.resume();
            }
        }

        public boolean isPaused() {
            return !isExpired() && waiter.isPaused();
        }

        protected ActiveCooldown() {
        }
    }
}
