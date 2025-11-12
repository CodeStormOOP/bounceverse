package com.github.codestorm.bounceverse.systems.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.almasb.fxgl.dsl.FXGL;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * UISystem với hiệu ứng sóng neon và nền "Siri" động.
 * Hệ thống này vẽ hiệu ứng LÊN TRÊN nền đen có sẵn của GameScene.
 */
public final class UISystem extends InitialSystem {

    // --- Hằng số điều khiển Sóng Neon ---
    private static final double WAVE_Y_POSITION = FXGL.getAppHeight() / 2.0;
    private static final double WAVE_AMPLITUDE = 50.0;
    private static final double WAVE_STROKE_WIDTH = 3.0;
    private static final double WAVE_SEPARATION = 12.0;
    private static final double WAVE_TIME_OFFSET = 0.4;
    private static final double WAVE_STEP = 20.0;
    private static final double WAVE_SPEED = 0.8;
    private static final double WAVE_BLUR_AMOUNT = 15.0;

    // --- Hằng số điều khiển hiệu ứng nền Siri ---
    private static final int BLOB_COUNT = 5;
    private static final double BLOB_BASE_SIZE = 250.0;
    private static final double BLOB_MAX_SPEED = 0.4;
    private static final double BLOB_GAUSSIAN_BLUR = 150.0;

    // --- Hằng số mục tiêu ---
    private static final Set<Color> TARGET_COLORS = Set.of(
            Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW
    );

    // --- Các biến thành viên ---
    private Group backgroundBlobLayer;
    private List<Node> blobs;
    private final Random random = new Random();

    private Group waveLayer;
    private double time = 0;
    private final Set<Color> collectedColors = new java.util.LinkedHashSet<>();
    private boolean allColorsCollected = false;

    private UISystem() {}

    public static UISystem getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void apply() {
        // 1. Khởi tạo các layer hiệu ứng (KHÔNG CÒN TẠO NỀN ĐEN Ở ĐÂY)
        createSiriBlobs();
        createWaveLayer();

        // 2. Thêm các layer hiệu ứng vào GỐC của scene để chúng nằm dưới các thực thể game
        FXGL.getGameScene().getRoot().getChildren().addAll(0,
                java.util.Arrays.asList(backgroundBlobLayer, waveLayer)
        );

        // 3. Bắt đầu vòng lặp animation
        startAnimationLoop();
    }

    private void createSiriBlobs() {
        backgroundBlobLayer = new Group();
        blobs = new ArrayList<>();
        Color[] siriColors = {Color.web("#26F3FF"), Color.web("#A942FF"), Color.web("#FF34BB")};
        for (int i = 0; i < BLOB_COUNT; i++) {
            Circle blob = new Circle();
            blob.setRadius(BLOB_BASE_SIZE + random.nextDouble() * 100);
            blob.setCenterX(random.nextDouble() * FXGL.getAppWidth());
            blob.setCenterY(random.nextDouble() * FXGL.getAppHeight());
            blob.setFill(siriColors[i % siriColors.length].deriveColor(0, 1, 1, 0.3));
            Point2D velocity = new Point2D((random.nextDouble() - 0.5) * 2 * BLOB_MAX_SPEED, (random.nextDouble() - 0.5) * 2 * BLOB_MAX_SPEED);
            blob.setUserData(velocity);
            blobs.add(blob);
        }
        backgroundBlobLayer.getChildren().addAll(blobs);
        backgroundBlobLayer.setEffect(new GaussianBlur(BLOB_GAUSSIAN_BLUR));
        backgroundBlobLayer.setBlendMode(BlendMode.ADD);
        backgroundBlobLayer.setVisible(false);
    }

    private void createWaveLayer() {
        waveLayer = new Group();
        waveLayer.setOpacity(0.9);
        waveLayer.setEffect(new GaussianBlur(WAVE_BLUR_AMOUNT));
        waveLayer.setBlendMode(BlendMode.ADD);
    }

    private void updateSiriBackground() {
        if (!allColorsCollected) return;

        if (!backgroundBlobLayer.isVisible()) {
            backgroundBlobLayer.setVisible(true);
        }

        double appW = FXGL.getAppWidth();
        double appH = FXGL.getAppHeight();
        for (Node node : blobs) {
            Circle blob = (Circle) node;
            Point2D v = (Point2D) blob.getUserData();
            blob.setCenterX(blob.getCenterX() + v.getX());
            blob.setCenterY(blob.getCenterY() + v.getY());
            if (blob.getCenterX() - blob.getRadius() < 0 || blob.getCenterX() + blob.getRadius() > appW) {
                v = new Point2D(-v.getX(), v.getY());
            }
            if (blob.getCenterY() - blob.getRadius() < 0 || blob.getCenterY() + blob.getRadius() > appH) {
                v = new Point2D(v.getX(), -v.getY());
            }
            blob.setUserData(v);
        }
    }

    private Path createWavePath(Color color, double timeOffset, double yOffset) {
        Path wavePath = new Path();
        wavePath.setStroke(color);
        wavePath.setStrokeWidth(WAVE_STROKE_WIDTH);
        wavePath.setFill(null);
        wavePath.setSmooth(true);
        double width = FXGL.getAppWidth();
        double step = WAVE_STEP;
        double adjustedTime = time + timeOffset;
        double baseY = WAVE_Y_POSITION + yOffset;
        double firstX = -step;
        double firstY = baseY + Math.sin(firstX / 100 + adjustedTime * WAVE_SPEED) * WAVE_AMPLITUDE;
        wavePath.getElements().add(new MoveTo(firstX, firstY));
        for (double x = 0; x <= width + step; x += step) {
            double prevX = x - step;
            double prevY = baseY + Math.sin(prevX / 100 + adjustedTime * WAVE_SPEED) * WAVE_AMPLITUDE;
            double currentY = baseY + Math.sin(x / 100 + adjustedTime * WAVE_SPEED) * WAVE_AMPLITUDE;
            wavePath.getElements().add(new CubicCurveTo(prevX + step / 2, prevY, x - step / 2, currentY, x, currentY));
        }
        return wavePath;
    }

    public void addColorToWave(Color color) {
        if (color != null && !allColorsCollected) {
            if (collectedColors.add(color)) {
                if (collectedColors.containsAll(TARGET_COLORS)) {
                    allColorsCollected = true;
                    System.out.println("Tất cả các màu đã được thu thập! Kích hoạt nền động.");
                }
            }
        }
    }

    private void updateWaves() {
        waveLayer.getChildren().clear();
        Path whiteWave = createWavePath(Color.WHITE, 0.0, 0.0);
        waveLayer.getChildren().add(whiteWave);
        int i = 0;
        for (Color brickColor : collectedColors) {
            double yOffset = (i + 1) * WAVE_SEPARATION;
            double timeOffsetValue = (i + 1) * WAVE_TIME_OFFSET;
            Path coloredWave = createWavePath(brickColor, timeOffsetValue, yOffset);
            waveLayer.getChildren().add(coloredWave);
            i++;
        }
    }

    private void startAnimationLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.01;
                updateWaves();
                updateSiriBackground();
            }
        }.start();
    }

    private static final class Holder {
        static final UISystem INSTANCE = new UISystem();
    }
}