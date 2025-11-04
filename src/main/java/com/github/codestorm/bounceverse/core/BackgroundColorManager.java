package com.github.codestorm.bounceverse.core;

import com.github.codestorm.bounceverse.core.systems.UISystem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <h1>{@link BackgroundColorManager}</h1>
 *
 * Quản lý hiệu ứng đổi màu nền khi phá brick:
 * <ul>
 * <li>Mỗi lần brick bị phá → nền chuyển màu mượt.</li>
 * <li>Màu nền phản ánh tiến độ phá gạch (xanh → đỏ).</li>
 * </ul>
 */
public final class BackgroundColorManager {

    private static int totalBricks;
    private static int remainingBricks;
    private static Rectangle backgroundRect;

    private BackgroundColorManager() {
    }

    /**
     * Khởi tạo hệ thống màu nền.
     *
     * @param total Tổng số brick trong màn chơi
     */
    public static void init(int total) {
        totalBricks = total;
        remainingBricks = total;

        backgroundRect = UISystem.getInstance().getBackgroundRect();
    }

    /**
     * Gọi hàm này mỗi khi 1 brick bị phá.
     */
    public static void brickDestroyed() {
        if (remainingBricks <= 0 || backgroundRect == null)
            return;

        remainingBricks = Math.max(0, remainingBricks - 1);
        double progress = 1.0 - (remainingBricks * 1.0 / totalBricks);

        // 🎨 Hue chuyển dần từ xanh (200°) sang đỏ (0°)
        Color targetColor = Color.hsb(200 - progress * 200, 0.8, 0.6 + 0.4 * progress);
        Color currentColor = (Color) backgroundRect.getFill();

        animateBackgroundChange(currentColor, targetColor);
    }

    /**
     * Hiệu ứng đổi màu nền mượt.
     */
    private static void animateBackgroundChange(Color from, Color to) {
        if (backgroundRect == null)
            return;

        final double duration = 0.8; // giây
        final long startTime = System.nanoTime();

        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsed = (now - startTime) / 1_000_000_000.0;
                double progress = Math.min(1.0, elapsed / duration);

                Color current = from.interpolate(to, progress);
                backgroundRect.setFill(current);

                if (progress >= 1.0) {
                    stop();
                }
            }
        }.start();
    }

}
