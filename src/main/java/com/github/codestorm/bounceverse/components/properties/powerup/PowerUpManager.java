package com.github.codestorm.bounceverse.components.properties.powerup;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Quản lý các Power-Up đang hoạt động: thời gian, gia hạn, và hiển thị HUD đếm
 * ngược.
 * Dùng singleton.
 */
public final class PowerUpManager {

    /** Mỗi power-up có timer riêng + logic hết hạn + UI text. */
    private static final class ActivePowerUp {
        final TimerAction timer;
        final Runnable onExpire;
        final Text label;
        double timeLeft; // giây còn lại

        ActivePowerUp(TimerAction timer, Runnable onExpire, Text label, double durationSeconds) {
            this.timer = timer;
            this.onExpire = onExpire;
            this.label = label;
            this.timeLeft = durationSeconds;
        }
    }

    private final Map<String, ActivePowerUp> activePowerUps = new HashMap<>();

    /** HUD hiển thị danh sách power-up. */
    private final VBox hudBox = new VBox(6);
    private boolean hudAdded = false;

    private static final PowerUpManager INSTANCE = new PowerUpManager();

    private PowerUpManager() {
        // Cấu hình vị trí HUD ở góc phải trên cùng
        hudBox.setTranslateX(FXGL.getAppWidth() - 220);
        hudBox.setTranslateY(20);
    }

    public static PowerUpManager getInstance() {
        return INSTANCE;
    }

    /** Đảm bảo HUD đã được thêm vào Scene (gọi an toàn mỗi frame). */
    private void ensureHUDAdded() {
        if (!hudAdded && FXGL.getGameScene() != null) {
            FXGL.getGameScene().addUINode(hudBox);
            hudAdded = true;
        }
    }

    /**
     * Kích hoạt hoặc gia hạn Power-Up.
     *
     * @param name       Tên power-up (ví dụ: "ExpandPaddle")
     * @param duration   Thời gian hiệu lực
     * @param onActivate Logic khi kích hoạt
     * @param onExpire   Logic khi hết hiệu lực
     */
    public void activate(String name, Duration duration, Runnable onActivate, Runnable onExpire) {
        ensureHUDAdded();

        // Nếu đã tồn tại power-up cùng loại -> xóa cũ
        if (activePowerUps.containsKey(name)) {
            var old = activePowerUps.get(name);
            if (!old.timer.isExpired()) {
                old.timer.expire();
            }
            old.onExpire.run();
            hudBox.getChildren().remove(old.label);
            activePowerUps.remove(name);
        }

        // Kích hoạt mới
        onActivate.run();

        // Tạo label UI hiển thị
        Text label = FXGL.getUIFactoryService()
                .newText(name + " " + String.format("%.1fs", duration.toSeconds()), 18);
        label.getStyleClass().add("powerup-text");
        hudBox.getChildren().add(label);

        // Tạo timer hết hạn
        var timer = FXGL.getGameTimer().runOnceAfter(() -> {
            onExpire.run();
            hudBox.getChildren().remove(label);
            activePowerUps.remove(name);
        }, duration);

        // Lưu lại
        activePowerUps.put(name,
                new ActivePowerUp(timer, onExpire, label, duration.toSeconds()));
    }

    /**
     * Gọi trong vòng lặp update() để giảm thời gian đếm ngược và cập nhật UI.
     */
    public void onUpdate(double tpf) {
        ensureHUDAdded(); // ✅ đảm bảo HUD đã được thêm vào scene

        for (var entry : activePowerUps.values()) {
            entry.timeLeft -= tpf;
            if (entry.timeLeft < 0)
                entry.timeLeft = 0;

            // Giữ nguyên tên power-up, chỉ cập nhật thời gian
            entry.label.setText(String.format("%s  %.1fs",
                    entry.label.getText().split(" ")[0], entry.timeLeft));
        }
    }

    /** Xóa toàn bộ power-up đang hoạt động (ví dụ khi mất bóng). */
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
}
