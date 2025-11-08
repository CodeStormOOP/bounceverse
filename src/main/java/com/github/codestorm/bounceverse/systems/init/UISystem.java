package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.*;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * UISystem với nền synthwave tối, wave neon cyan-magenta chuyển động.
 */
public final class UISystem extends InitialSystem {

    private Rectangle backgroundRect;
    private Group waveLayer;
    private double time = 0;

    public static UISystem getInstance() {
        return UISystem.Holder.INSTANCE;
    }

    @Override
    public void apply() {
        double width = FXGL.getAppWidth();
        double height = FXGL.getAppHeight();

        // 🌌 Nền chính tối
        backgroundRect = new Rectangle(width, height, Color.web("#0d0b1a"));
        backgroundRect.setEffect(new GaussianBlur(80));

        // 🌊 Lớp sóng neon
        waveLayer = new Group();
        waveLayer.setOpacity(0.4);
        waveLayer.setEffect(new GaussianBlur(60));

        FXGL.getGameScene().getContentRoot().getChildren().add(0, backgroundRect);
        FXGL.getGameScene().getContentRoot().getChildren().add(1, waveLayer);

        startWaveAnimation(width, height);
    }

    private void startWaveAnimation(double width, double height) {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.016;

                // ✨ Sóng cyan–magenta nhấp nháy mờ
                updateWaves(width, height);
            }
        }.start();
    }

    private void updateWaves(double width, double height) {
        waveLayer.getChildren().clear();

        int waveCount = 3;
        for (int i = 0; i < waveCount; i++) {
            double speed = 1 + i * 0.15;
            double amplitude = 40 + i * 25;
            double offsetY = height / 2 + Math.sin(time * 0.4 + i) * 50;

            Path wave = new Path();
            wave.setStrokeWidth(150 - i * 25);

            // 💜💙 Màu neon hòa trộn tím–xanh
            Color c = i % 2 == 0
                    ? Color.web("#00fff7", 0.25 + 0.1 * i)
                    : Color.web("#ff007c", 0.25 + 0.1 * i);

            wave.setStroke(c);
            wave.setFill(Color.TRANSPARENT);

            double step = width / 4;
            wave.getElements().add(new MoveTo(0, offsetY));

            for (int x = 0; x <= width + step; x += step) {
                double controlY = offsetY + Math.sin(time * speed + x * 0.005 + i) * amplitude;
                double nextX = x + step;
                double nextY = offsetY + Math.sin(time * speed + (x + step) * 0.005 + i) * amplitude;
                wave.getElements().add(new CubicCurveTo(
                        x + step / 2, controlY, nextX - step / 2, nextY, nextX, nextY));
            }

            waveLayer.getChildren().add(wave);
        }
    }

    public Rectangle getBackgroundRect() {
        return backgroundRect;
    }

    private static final class Holder {
        static final UISystem INSTANCE = new UISystem();
    }
}
