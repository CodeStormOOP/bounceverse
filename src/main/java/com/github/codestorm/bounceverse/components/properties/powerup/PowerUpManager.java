package com.github.codestorm.bounceverse.components.properties.powerup;

import java.util.HashMap;
import java.util.Map;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Quản lý các Power-Up đang hoạt động: thời gian, gia hạn, và hiển thị HUD đếm
 * ngược.
 * Dùng singleton.
 */
public final class PowerUpManager {

    /**
     * Lớp nội bộ để theo dõi mỗi power-up đang hoạt động.
     * Sửa đổi: 'timer' không còn là final để có thể cập nhật khi cộng dồn thời
     * gian.
     */
    private static final class ActivePowerUp {
        TimerAction timer;
        final Runnable onExpire;
        final Text label;
        double timeLeft;

        ActivePowerUp(TimerAction timer, Runnable onExpire, Text label, double durationSeconds) {
            this.timer = timer;
            this.onExpire = onExpire;
            this.label = label;
            this.timeLeft = durationSeconds;
        }
    }

    private final Map<String, ActivePowerUp> activePowerUps = new HashMap<>();
    private final VBox hudBox = new VBox(6);
    private boolean hudAdded = false;

    private static final PowerUpManager INSTANCE = new PowerUpManager();

    private PowerUpManager() {
        hudBox.setTranslateX(FXGL.getAppWidth() - 220);
        hudBox.setTranslateY(20);
    }

    public static PowerUpManager getInstance() {
        return INSTANCE;
    }

    private void ensureHUDAdded() {
        if (!hudAdded && FXGL.getGameScene() != null) {
            FXGL.getGameScene().addUINode(hudBox);
            hudAdded = true;
        }
    }

    /**
     * Kích hoạt hoặc gia hạn (cộng dồn) thời gian cho một Power-Up.
     *
     * @param name       Tên power-up
     * @param duration   Thời gian hiệu lực của power-up mới
     * @param onActivate Logic khi kích hoạt LẦN ĐẦU
     * @param onExpire   Logic khi hết hiệu lực hoàn toàn
     */
    public void activate(String name, Duration duration, Runnable onActivate, Runnable onExpire) {
        ensureHUDAdded();
        double newDurationSeconds = duration.toSeconds();

        if (activePowerUps.containsKey(name)) {
            var existing = activePowerUps.get(name);

            existing.timer.expire();

            double newTimeLeft = existing.timeLeft + newDurationSeconds;
            existing.timeLeft = newTimeLeft;

            var newTimer = FXGL.getGameTimer().runOnceAfter(() -> {
                onExpire.run();
                hudBox.getChildren().remove(existing.label);
                activePowerUps.remove(name);
            }, Duration.seconds(newTimeLeft));

            existing.timer = newTimer;

            return;
        }

        onActivate.run();

        Text label = FXGL.getUIFactoryService()
                .newText(name + " " + String.format("%.1fs", newDurationSeconds), 18);
        label.getStyleClass().add("powerup-text");
        hudBox.getChildren().add(label);

        var timer = FXGL.getGameTimer().runOnceAfter(() -> {
            onExpire.run();
            hudBox.getChildren().remove(label);
            activePowerUps.remove(name);
        }, duration);

        activePowerUps.put(name, new ActivePowerUp(timer, onExpire, label, newDurationSeconds));
    }

    public void onUpdate(double tpf) {
        ensureHUDAdded();

        for (var entry : activePowerUps.values()) {
            entry.timeLeft -= tpf;
            if (entry.timeLeft < 0)
                entry.timeLeft = 0;

            String powerUpName = entry.label.getText().split(" ")[0];
            entry.label.setText(String.format("%s  %.1fs", powerUpName, entry.timeLeft));
        }
    }

    public void clearAll() {
        ensureHUDAdded();

        activePowerUps.values().forEach(entry -> {
            if (!entry.timer.isExpired()) {
                entry.timer.expire();
            }
            entry.onExpire.run();
            hudBox.getChildren().remove(entry.label);
        });
        activePowerUps.clear();
    }

    /**
     * Hủy bỏ và dọn dẹp một Power-Up cụ thể đang hoạt động theo tên.
     * 
     * @param name Tên của power-up cần hủy
     */
    public void clearPowerUp(String name) {
        if (activePowerUps.containsKey(name)) {
            var existing = activePowerUps.get(name);

            if (!existing.timer.isExpired()) {
                existing.timer.expire();
            }

            existing.onExpire.run();

            hudBox.getChildren().remove(existing.label);

            activePowerUps.remove(name);
        }
    }
}