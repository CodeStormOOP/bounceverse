package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import com.github.codestorm.bounceverse.AssetsPath;
import com.github.codestorm.bounceverse.AssetsPath.Textures.Bricks.ColorAssets;
import com.github.codestorm.bounceverse.components.behaviors.Explosion;
import com.github.codestorm.bounceverse.components.behaviors.HealthDeath; // THÊM IMPORT QUAN TRỌNG NÀY
import com.github.codestorm.bounceverse.components.behaviors.Special;
import com.github.codestorm.bounceverse.components.behaviors.brick.BrickDropPowerUp;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddlePowerComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.factory.entities.*;
import com.github.codestorm.bounceverse.scenes.subscenes.ingame.DeathSubscene;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import com.github.codestorm.bounceverse.typing.structures.HealthIntValue;
import com.github.codestorm.bounceverse.ui.elements.HorizontalPositiveInteger;
import com.github.codestorm.bounceverse.ui.elements.ingame.Hearts;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import libs.FastNoiseLite;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class GameSystem extends InitialSystem {

    private GameSystem() {
    }

    public static GameSystem getInstance() {
        return Holder.INSTANCE;
    }

    public static void nextLevel() {
        FXGL.getGameTimer().clear();
        PowerUpManager.getInstance().clearAll();

        FXGL.inc("level", 1);
        FXGL.set("seed", FXGL.geti("seed") + 1);

        FXGL.getNotificationService().pushNotification("Level " + FXGL.geti("level"));

        // Dọn dẹp tất cả thực thể động
        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL, EntityType.BRICK, EntityType.POWER_UP, EntityType.BULLET).forEach(Entity::removeFromWorld);

        // Tạo lại màn chơi sau một khoảng trễ nhỏ để đảm bảo mọi thứ đã được dọn dẹp
        FXGL.runOnce(() -> {
            EntitySpawn.paddle();
            EntitySpawn.ball();
            EntitySpawn.bricks();
        }, Duration.seconds(0.1));
    }

    /**
     * Reset lại toàn bộ game về trạng thái ban đầu.
     */
    public static void resetGame() {
        // Cách làm đúng và an toàn nhất là yêu cầu engine bắt đầu lại từ đầu
        FXGL.getGameController().startNewGame();
    }

    public static final class Variables {

        private Variables() {
        }
        public static final int MAX_LIVES = 3;
        public static final int DEFAULT_LIVES = 3;
        public static final int DEFAULT_SCORE = 0;
        public static final double DEFAULT_BALL_SPEED = 300;
        private static final List<String> PADDLE_COLORS = List.of("blue", "green", "orange", "pink", "red", "yellow");

        public static final SaveLoadHandler SAVE_LOAD_HANDLER = new SaveLoadHandler() {
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
                final var lives = new HealthIntValue(GameSystem.Variables.MAX_LIVES, intLives);
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

        public static void loadDefault(com.almasb.fxgl.core.collection.PropertyMap vars) {
            final var lives = new HealthIntValue(MAX_LIVES, DEFAULT_LIVES);
            vars.clear();
            vars.setValue("lives", lives);
            vars.setValue("score", DEFAULT_SCORE);
            vars.setValue("level", 1);
            vars.setValue("seed", (int) (System.currentTimeMillis() & 0x7FFFFFFF));
            vars.setValue("ballSpeed", DEFAULT_BALL_SPEED);
            String randomColor = PADDLE_COLORS.get(FXGLMath.random(0, PADDLE_COLORS.size() - 1));
            vars.setValue("paddleColor", randomColor);
        }

        public static void hookDeathSubscene() {
            final HealthIntValue livesProperty = FXGL.getWorldProperties().getObject("lives");
            livesProperty.onZeroListeners.add(() -> {
                final var score = FXGL.getWorldProperties().getInt("score");
                final var level = FXGL.getWorldProperties().getInt("level");
                final var deathSubscene = new DeathSubscene(score, level);
                deathSubscene.onGotoMainMenuListeners.add(() -> {
                    FXGL.getSceneService().popSubScene();
                    FXGL.getGameController().resumeEngine();
                });
                FXGL.getSceneService().pushSubScene(deathSubscene);
                FXGL.getGameController().pauseEngine();
            });
        }
    }

    private static final class EntitySpawn {

        private EntitySpawn() {
        }

        public static void addFactory() {
            FXGL.getGameWorld().addEntityFactory(new WallFactory());
            FXGL.getGameWorld().addEntityFactory(new BrickFactory());
            FXGL.getGameWorld().addEntityFactory(new BulletFactory());
            FXGL.getGameWorld().addEntityFactory(new PaddleFactory());
            FXGL.getGameWorld().addEntityFactory(new BallFactory());
            FXGL.getGameWorld().addEntityFactory(new PowerUpFactory());
        }

        public static void walls() {
            FXGL.spawn("wallTop");
            FXGL.spawn("wallBottom");
            FXGL.spawn("wallLeft");
            FXGL.spawn("wallRight");
        }

        public static void bricks() {
            final var seed = FXGL.getWorldProperties().getInt("seed");
            var noise = new FastNoiseLite(seed);

            var colors = AssetsPath.Textures.Bricks.COLORS.values()
                    .stream()
                    .map(ColorAssets::color)
                    .toArray(Color[]::new);

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
            if (brickW < minW) {
                brickW = minW;
            }
            if (brickW > maxW) {
                brickW = maxW;
            }
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

            record Cell(int gx, int gy, double x, double y, float n) {

            }
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

            // *** BẮT ĐẦU THAY ĐỔI LOGIC TẠI ĐÂY ***
            // 1. Danh sách các loại gạch BẮT BUỘC. Bạn có thể thêm/bớt ở đây.
            final List<String> requiredTypes = new ArrayList<>(List.of(
                    "explodingBrick", // Thêm gạch nổ
                    "explodingBrick",
                    "explodingBrick",
                    "keyBrick", // Thêm gạch chìa khóa
                    "keyBrick",
                    "shieldBrick",
                    "strongBrick",
                    "normalBrick"
            ));
            // Xáo trộn danh sách để vị trí của chúng ngẫu nhiên
            Collections.shuffle(requiredTypes, new Random(seed));

            var target = Math.min(amount, cells.size());
            var colorAssignments = new Color[target];
            System.arraycopy(colors, 0, colorAssignments, 0, Math.min(colors.length, target));
            for (var i = colors.length; i < target; i++) {
                var n = cells.get(i).n();
                var normalizedN = (n + 1.0f) / 2.0f;
                var colorIndex = (int) (normalizedN * colors.length);
                if (colorIndex >= colors.length) {
                    colorIndex = colors.length - 1;
                }
                colorAssignments[i] = colors[colorIndex];
            }
            for (var i = 0; i < target; i++) {
                var c = cells.get(i);
                var n = c.n();
                final String type;

                // 2. Ưu tiên spawn các loại gạch bắt buộc
                if (i < requiredTypes.size()) {
                    type = requiredTypes.get(i);
                } else {
                    // 3. Logic ngẫu nhiên cho các viên gạch còn lại, có thêm gạch nổ
                    if (n > 0.6f) {
                        type = "shieldBrick";
                    } else if (n > 0.3f) {
                        type = "strongBrick";
                    } else if (n > 0.0f) {
                        type = "explodingBrick";
                    } else {
                        type = "normalBrick";
                    }
                }

                var data = new SpawnData();
                data.put("pos", new Point2D(c.x(), c.y()));
                data.put("width", brickWidth);
                data.put("height", brickHeight);
                data.put("color", colorAssignments[i]);
                FXGL.spawn(type, data);
            }
            // *** KẾT THÚC THAY ĐỔI ***
        }

        public static void paddle() {
            FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(Entity::removeFromWorld);
            FXGL.spawn("paddle");
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

    public static final class UI {

        private UI() {
        }
        private boolean isInitialized = false;
        private Hearts heartsDisplay;
        private HorizontalPositiveInteger scoreDisplay;
        private Text levelDisplay;
        private Text cooldownText;

        public static UI getInstance() {
            return Holder.INSTANCE;
        }

        public void addScoreDisplay() {
            if (scoreDisplay == null) {
                final var scoreProperty = FXGL.getWorldProperties().intProperty("score");
                scoreDisplay = new HorizontalPositiveInteger(scoreProperty);
                scoreDisplay.setMinDigits(6);
            }
            FXGL.getGameScene().addUINode(scoreDisplay.getView());
            scoreDisplay.getView().setTranslateX(20);
            scoreDisplay.getView().setTranslateY(20);
        }

        public void addHeartsDisplay() {
            if (heartsDisplay == null) {
                heartsDisplay = new Hearts();
            }
            FXGL.getGameScene().addUINode(heartsDisplay.getView());
            heartsDisplay.getView().setTranslateX(20);
            heartsDisplay.getView().setTranslateY(FXGL.getAppHeight() - 52);
        }

        public void addLevelDisplay() {
            if (levelDisplay == null) {
                final var levelProperty = FXGL.getWorldProperties().intProperty("level");
                levelDisplay = FXGL.getUIFactoryService().newText("", Color.WHITE, 24.0);
                levelDisplay.textProperty().bind(levelProperty.asString("LEVEL %d"));
            }
            FXGL.getGameScene().addUINode(levelDisplay);
            levelDisplay.setTranslateX(FXGL.getAppWidth() - 120);
            levelDisplay.setTranslateY(FXGL.getAppHeight() - 30);
        }

        public void addCooldownDisplay() {
            if (cooldownText == null) {
                cooldownText = FXGL.getUIFactoryService().newText("", 18);
                cooldownText.getStyleClass().add("powerup-text");
            }
            FXGL.getGameScene().addUINode(cooldownText);
            cooldownText.setTranslateX(FXGL.getAppWidth() - 220);
            cooldownText.setTranslateY(FXGL.getAppHeight() / 2);
        }

        public void onUpdate() {
            if (!isInitialized || cooldownText == null) {
                return;
            }
            var paddleOpt = FXGL.getGameWorld().getSingletonOptional(EntityType.PADDLE);
            if (paddleOpt.isEmpty()) {
                cooldownText.setText("");
                return;
            }
            var powerCompOpt = paddleOpt.get().getComponentOptional(PaddlePowerComponent.class);
            if (powerCompOpt.isEmpty()) {
                cooldownText.setText("");
                return;
            }
            var powerComp = powerCompOpt.get();
            var powerCooldown = powerComp.getPowerCooldown();
            var timeLeft = powerCooldown.getTimeLeft();
            if (timeLeft == null || timeLeft.toMillis() <= 0) {
                cooldownText.setText("Power: Start");
            } else {
                cooldownText.setText(String.format("Power: Cooldown"));
            }
        }

        public void addAll() {
            if (isInitialized) {
                return;
            }
            addScoreDisplay();
            addHeartsDisplay();
            addLevelDisplay();
            addCooldownDisplay();
            isInitialized = true;
        }

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
            if (cooldownText != null) {
                FXGL.getGameScene().removeUINode(cooldownText);
            }
            isInitialized = false;
        }

        public void dispose() {
            removeAll();
            scoreDisplay = null;
            heartsDisplay = null;
            levelDisplay = null;
            cooldownText = null;
        }

        private static final class Holder {

            static final UI INSTANCE = new UI();
        }
    }

    @Override
    public void apply() {
        Variables.hookDeathSubscene();
        EntitySpawn.addFactory();
        EntitySpawn.walls();
        EntitySpawn.bricks();
        EntitySpawn.paddle();
        EntitySpawn.ball();
        UI.getInstance().addAll();
    }

    private static final class Holder {

        static final GameSystem INSTANCE = new GameSystem();
    }
}
