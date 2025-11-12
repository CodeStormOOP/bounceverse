package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import com.github.codestorm.bounceverse.AssetsPath;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.factory.entities.BrickFactory;
import com.github.codestorm.bounceverse.factory.entities.BulletFactory;
import com.github.codestorm.bounceverse.factory.entities.PaddleFactory;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.scenes.subscenes.ingame.DeathSubscene;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import com.github.codestorm.bounceverse.typing.structures.HealthIntValue;
import com.github.codestorm.bounceverse.ui.elements.HorizontalPositiveInteger;
import com.github.codestorm.bounceverse.ui.elements.ingame.Hearts;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import libs.FastNoiseLite;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 *
 *
 * <h1>{@link GameSystem}</h1>
 *
 * Hệ thống khởi tạo Game. <br>
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 */
public final class GameSystem extends InitialSystem {
    private GameSystem() {}

    public static GameSystem getInstance() {
        return GameSystem.Holder.INSTANCE;
    }

    /** Xử lý lưu trữ biến toàn cục trong Game. */
    public static final class Variables {
        private Variables() {}

        public static final int MAX_LIVES = 5;
        public static final int DEFAULT_LIVES = 3;
        public static final int DEFAULT_SCORE = 0;
        public static final SaveLoadHandler SAVE_LOAD_HANDLER =
                new SaveLoadHandler() {
                    @Override
                    public void onSave(@NotNull DataFile dataFile) {
                        final var properties = FXGL.getWorldProperties();
                        final HealthIntValue lives = properties.getObject("lives");
                        final int score = properties.getInt("score");
                        final int level = properties.getInt("level");
                        final int seed = properties.getInt("seed");

                        final var bundle = new Bundle("game");
                        bundle.put("lives", lives.getValue());
                        bundle.put("score", score);
                        bundle.put("level", level);
                        bundle.put("seed", seed);
                        dataFile.putBundle(bundle);
                    }

                    @Override
                    public void onLoad(@NotNull DataFile dataFile) {
                        final var bundle = dataFile.getBundle("game");
                        final int intLives = bundle.get("lives");
                        final var lives =
                                new HealthIntValue(GameSystem.Variables.MAX_LIVES, intLives);
                        final int score = bundle.get("score");
                        final int level = bundle.get("level");
                        final int seed = bundle.get("seed");

                        final var vars = FXGL.getWorldProperties();
                        vars.clear();
                        vars.setValue("lives", lives);
                        vars.setValue("score", score);
                        vars.setValue("level", level);
                        vars.setValue("seed", seed);
                    }
                };

        /**
         * Tải biến lưu trữ mặc định vào Game. <br>
         *
         * @apiNote Sử dụng {@link FXGL#getWorldProperties()}
         * @param vars Map biến
         */
        public static void loadDefault(Map<String, Object> vars) {
            final var lives = new HealthIntValue(MAX_LIVES, DEFAULT_LIVES);

            vars.clear();
            vars.put("lives", lives);
            vars.put("score", DEFAULT_SCORE);
            vars.put("level", 1);
            vars.put("seed", (int) (System.currentTimeMillis() & 0x7FFFFFFF));
        }

        public static void hookDeathSubscene() {
            final HealthIntValue livesProperty = FXGL.getWorldProperties().getObject("lives");
            livesProperty.onZeroListeners.add(
                    () -> {
                        final var score = FXGL.getWorldProperties().getInt("score");
                        final var level = FXGL.getWorldProperties().getInt("level");

                        final var deathSubscene = new DeathSubscene(score, level);
                        deathSubscene.onGotoMainMenuListeners.add(
                                () -> {
                                    FXGL.getSceneService().popSubScene();
                                    FXGL.getGameController().resumeEngine();
                                });

                        FXGL.getSceneService().pushSubScene(deathSubscene);
                        FXGL.getGameController().pauseEngine();
                    });
        }
    }

    /** Phương thức spawn entities trong Game. */
    private static final class EntitySpawn {
        private EntitySpawn() {}

        public static void addFactory() {
            FXGL.getGameWorld().addEntityFactory(new WallFactory());
            FXGL.getGameWorld().addEntityFactory(new BrickFactory());
            FXGL.getGameWorld().addEntityFactory(new BulletFactory());
            FXGL.getGameWorld().addEntityFactory(new PaddleFactory());
            FXGL.getGameWorld().addEntityFactory(new BallFactory());
        }

        public static void walls() {
            FXGL.spawn("wallTop");
            FXGL.spawn("wallBottom");
            FXGL.spawn("wallLeft");
            FXGL.spawn("wallRight");
        }

        public static void brick() {
            final var seed = FXGL.getWorldProperties().getInt("seed");
            var noise = new FastNoiseLite(seed);
            var colors = AssetsPath.Textures.Bricks.COLORS.keySet().toArray(new Color[0]);

            noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            noise.SetFrequency(0.2f);
            noise.SetFractalType(FastNoiseLite.FractalType.FBm);
            noise.SetFractalOctaves(4);
            noise.SetFractalLacunarity(2.1f);
            noise.SetFractalGain(0.6f);
            noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            noise.SetDomainWarpAmp(6.0f);

            final var rows = 5;
            final var cols = 10;
            final var amount = 36;

            final var appWidth = FXGL.getAppWidth();
            final var appHeight = FXGL.getAppHeight();
            final double marginX = 40;
            final double startY = 60;
            final var verticalCoverageRatio = 0.30;
            final double spacingX = 5;
            final double spacingY = 5;
            final var aspect = 30.0 / 80.0;

            var availableWidth = appWidth - 2 * marginX;
            var wFromWidth = (availableWidth - (cols - 1) * spacingX) / cols;
            var hFromWidth = wFromWidth * aspect;

            var targetBrickAreaHeight = appHeight * verticalCoverageRatio;
            var hMaxByHeight = (targetBrickAreaHeight - (rows - 1) * spacingY) / rows;

            var brickW = wFromWidth;
            var brickH = hFromWidth;

            if (brickH > hMaxByHeight) {
                brickH = hMaxByHeight;
                brickW = brickH / aspect;
            }

            double minW = 40, maxW = 160;
            if (brickW < minW) brickW = minW;
            if (brickW > maxW) brickW = maxW;
            brickH = brickW * aspect;

            var totalH = rows * brickH + (rows - 1) * spacingY;
            if (totalH > targetBrickAreaHeight) {
                brickH = hMaxByHeight;
                brickW = brickH / aspect;
            }

            var brickWidth = (int) Math.round(brickW);
            var brickHeight = (int) Math.round(brickH);

            var totalW = cols * brickWidth + (cols - 1) * spacingX;
            var startX = Math.max(marginX, (appWidth - totalW) / 2.0);

            // Lấy ra amount phần tử có độ cao cao nhất
            record Cell(int gx, int gy, double x, double y, float n) {}
            var cells = new ArrayList<Cell>(rows * cols);
            for (var gy = 0; gy < rows; gy++) {
                for (var gx = 0; gx < cols; gx++) {
                    var posX = startX + gx * (brickWidth + spacingX);
                    var posY = startY + gy * (brickHeight + spacingY);
                    var n = noise.GetNoise(gx, gy);
                    cells.add(new Cell(gx, gy, posX, posY, n));
                }
            }
            cells.sort(Comparator.comparing(Cell::n).reversed());
            var target = Math.min(amount, cells.size());

            // Đảm bảo mỗi màu xuất hiện ít nhất một lần
            var colorAssignments = new Color[target];

            // Bước 1: Gán mỗi màu cho ít nhất một brick
            System.arraycopy(colors, 0, colorAssignments, 0, Math.min(colors.length, target));

            // Bước 2: Phân phối các brick còn lại dựa trên noise
            for (var i = colors.length; i < target; i++) {
                var c = cells.get(i);
                var n = c.n();
                var normalizedN = (n + 1.0f) / 2.0f; // [0, 1]

                var colorIndex = (int) (normalizedN * colors.length);
                if (colorIndex >= colors.length) {
                    colorIndex = colors.length - 1;
                }
                colorAssignments[i] = colors[colorIndex];
            }

            // Spawn các brick với màu đã được gán
            for (var i = 0; i < target; i++) {
                var c = cells.get(i);
                var n = c.n();

                final String type;
                if (n > 0.5f) {
                    type = "shieldBrick";
                } else if (n > 0.3f) {
                    type = "strongBrick";
                } else {
                    type = "normalBrick";
                }

                var data = new SpawnData();
                data.put("pos", new Point2D(c.x(), c.y()));
                data.put("width", brickWidth);
                data.put("height", brickHeight);
                data.put("color", colorAssignments[i]);
                FXGL.spawn(type, data);
            }
        }

        public static void paddle() {
            FXGL.getGameWorld()
                    .getEntitiesByType(EntityType.PADDLE)
                    .forEach(Entity::removeFromWorld);

            var px = FXGL.getAppWidth() / 2.0 - 60;
            double py = FXGL.getAppHeight() - 40;
            FXGL.spawn("paddle", px, py);
        }

        public static void ball() {
            if (FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).isEmpty()) {
                var paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);

                var x = paddle.getCenter().getX() - BallFactory.DEFAULT_RADIUS;
                var y = paddle.getY() - BallFactory.DEFAULT_RADIUS + 1;

                FXGL.spawn("ball", new SpawnData(x, y).put("attached", true));
                FXGL.set("ballAttached", true);
            }
        }
    }

    /**
     * Hiển thị UI/HUD trong Game.
     *
     * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
     */
    private static final class UI {
        private UI() {}

        private boolean isInitialized = false;
        private Hearts heartsDisplay;
        private HorizontalPositiveInteger scoreDisplay;
        private javafx.scene.text.Text levelDisplay;

        public static UI getInstance() {
            return Holder.INSTANCE;
        }

        /** Thêm Score display (hiển thị điểm số) vào game scene. */
        public void addScoreDisplay() {
            if (scoreDisplay == null) {
                final var scoreProperty = FXGL.getWorldProperties().intProperty("score");
                scoreDisplay = new HorizontalPositiveInteger(scoreProperty);
                scoreDisplay.setMinDigits(6);
            }

            FXGL.getGameScene().addUINode(scoreDisplay.getView());
            scoreDisplay.getView().setTranslateX(20);
            scoreDisplay.getView().setTranslateY(20); // Ở trên cùng
        }

        /** Thêm Hearts display (hiển thị số mạng) vào game scene. */
        public void addHeartsDisplay() {
            if (heartsDisplay == null) {
                heartsDisplay = new Hearts();
            }

            FXGL.getGameScene().addUINode(heartsDisplay.getView());
            heartsDisplay.getView().setTranslateX(20);
            heartsDisplay.getView().setTranslateY(FXGL.getAppHeight() - 52); // Dưới cùng màn hình
        }

        /** Thêm Level display (hiển thị level) vào game scene. */
        public void addLevelDisplay() {
            if (levelDisplay == null) {
                final var levelProperty = FXGL.getWorldProperties().intProperty("level");
                levelDisplay = FXGL.getUIFactoryService().newText("", Color.WHITE, 24.0);

                // Bind text to level property
                levelDisplay.textProperty().bind(levelProperty.asString("LEVEL %d"));
            }

            FXGL.getGameScene().addUINode(levelDisplay);
            // Position at bottom right corner
            levelDisplay.setTranslateX(FXGL.getAppWidth() - 120);
            levelDisplay.setTranslateY(FXGL.getAppHeight() - 30);
        }

        /** Thêm tất cả UI elements vào game scene. */
        public void addAll() {
            if (isInitialized) {
                return;
            }

            addScoreDisplay();
            addHeartsDisplay();
            addLevelDisplay();
            isInitialized = true;
        }

        /** Xóa tất cả UI elements khỏi game scene và giải phóng tài nguyên. */
        public void removeAll() {
            if (!isInitialized) {
                return;
            }

            if (scoreDisplay != null) {
                FXGL.getGameScene().removeUINode(scoreDisplay.getView());
            }

            if (heartsDisplay != null) {
                FXGL.getGameScene().removeUINode(heartsDisplay.getView());
            }

            if (levelDisplay != null) {
                FXGL.getGameScene().removeUINode(levelDisplay);
            }

            isInitialized = false;
        }

        /** Giải phóng hoàn toàn tài nguyên (xóa references). */
        public void dispose() {
            removeAll();
            scoreDisplay = null;
            heartsDisplay = null;
            levelDisplay = null;
        }

        /** Kiểm tra UI đã được khởi tạo chưa. */
        public boolean isInitialized() {
            return isInitialized;
        }

        /** Lazy-loaded singleton holder. */
        private static final class Holder {
            static final UI INSTANCE = new UI();
        }
    }

    @Override
    public void apply() {
        Variables.hookDeathSubscene();

        // Entity
        EntitySpawn.addFactory();
        EntitySpawn.walls();
        EntitySpawn.brick();
        EntitySpawn.paddle();
        EntitySpawn.ball();

        UI.getInstance().addAll();
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {

        static final GameSystem INSTANCE = new GameSystem();
    }
}
