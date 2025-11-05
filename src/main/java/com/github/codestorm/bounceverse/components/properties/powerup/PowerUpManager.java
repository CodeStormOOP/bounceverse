package com.github.codestorm.bounceverse.components.properties.powerup;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Quản lý các PowerUp đang hoạt động: thời gian, gia hạn, và hết hiệu lực.
 * Dùng singleton.
 */
public final class PowerUpManager {

    /** Mỗi loại power-up gắn với 1 timer riêng. */
    private final Map<String, TimerAction> activeTimers = new HashMap<>();

    private static final PowerUpManager INSTANCE = new PowerUpManager();

    private PowerUpManager() {}

    public static PowerUpManager getInstance() {
        return INSTANCE;
    }

    /**
     * Kích hoạt hoặc gia hạn PowerUp.
     * @param name  Tên power-up (ví dụ: "ExpandPaddle")
     * @param duration  Thời gian hiệu lực
     * @param onActivate  Logic khi kích hoạt
     * @param onExpire    Logic khi hết hiệu lực
     */
    public void activate(String name, Duration duration, Runnable onActivate, Runnable onExpire) {
        if (activeTimers.containsKey(name)) {
            var oldTimer = activeTimers.get(name);
            if (!oldTimer.isExpired()) {
                oldTimer.expire();
            }
        }

        onActivate.run();

        var timer = FXGL.getGameTimer().runOnceAfter(() -> {
            onExpire.run();
            activeTimers.remove(name);
        }, duration);

        activeTimers.put(name, timer);
    }

    public void clearAll() {
        activeTimers.values().forEach(TimerAction::expire);
        activeTimers.clear();
    }
}
