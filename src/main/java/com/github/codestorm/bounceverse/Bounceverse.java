package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.github.codestorm.bounceverse.components.properties.Move;
import com.github.codestorm.bounceverse.data.types.EntityType;
import com.github.codestorm.bounceverse.factory.BrickFactory;
import com.github.codestorm.bounceverse.factory.BulletFactory;
import com.github.codestorm.bounceverse.factory.PaddleFactory;
import com.github.codestorm.bounceverse.factory.SceneFactory;
import com.github.codestorm.bounceverse.factory.WallFactory;
import com.github.codestorm.bounceverse.systems.LaunchOption;
import com.github.codestorm.bounceverse.systems.physics.CollisionSystem;
import java.io.IOException;
import java.util.Properties;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 *
 *
 * <h1>{@link Bounceverse}</h1>
 *
 * Phần Hệ thống Chương trình chính của game, nơi mà mọi thứ bắt đầu...
 *
 * <p>
 * <i>Game {@link Bounceverse} được lấy cảm hứng từ game Arkanoid nổi tiếng, nơi
 * người chơi điều
 * khiển một thanh để đỡ bóng và phá vỡ các viên gạch. Mục tiêu của game là phá
 * vỡ tất cả các viên
 * gạch và dành được điểm số cao nhất. Nhưng liệu mọi thứ chỉ đơn giản như
 * vậy?</i>
 */
public final class Bounceverse extends GameApplication {
    private static LaunchOption launchOption;

    /**
     * Cấu hình game.
     *
     * <p>
     * Sử dụng {@link #loadConfigs()} để load các config.
     */
    private static final class Configs {
        private static final String ROOT = "/configs/";

        /** Cấu hình game bên trong hệ thống game. */
        private static final class System {
            public static Properties settings;

            private System() {
            }
        }

        /** Cấu hình game bên ngoài hệ thống game. */
        private static final class Options {
            public static Properties DEFAULT; // Cấu hình mặc định của trò chơi

            private Options() {
            }
        }

        /**
         * Load game configs.
         *
         * @throws IOException if an error occurred when reading from the input stream.
         */
        public static void loadConfigs() throws IOException {
            Options.DEFAULT = Utils.IO.loadProperties(ROOT + "default.properties");
            System.settings = Utils.IO.loadProperties(ROOT + "system/settings.properties");
        }

        private Configs() {
        }
    }

    @Override
    protected void initSettings(GameSettings settings) {
        try {
            Configs.loadConfigs();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Basic
        settings.setTitle(Configs.System.settings.getProperty("settings.name"));
        settings.setVersion(Configs.System.settings.getProperty("settings.version"));
        settings.setCredits(Utils.IO.readTextFile("credits.txt"));
        settings.setApplicationMode(
                Boolean.parseBoolean(Configs.System.settings.getProperty("settings.devMode"))
                        ? ApplicationMode.DEVELOPER
                        : (launchOption.isDebug())
                                ? ApplicationMode.DEBUG
                                : ApplicationMode.RELEASE);

        // Display
        settings.setWidth(Integer.parseInt(Configs.Options.DEFAULT.getProperty("width")));
        settings.setHeight(Integer.parseInt(Configs.Options.DEFAULT.getProperty("height")));
        settings.setFullScreenAllowed(true);

        // In-app
        settings.setSceneFactory(new SceneFactory());
        settings.setMainMenuEnabled(true);
        settings.setIntroEnabled(true);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BrickFactory());
        FXGL.getGameWorld().addEntityFactory(new BulletFactory());
        FXGL.getGameWorld().addEntityFactory(new PaddleFactory());
        FXGL.getGameWorld().addEntityFactory(new WallFactory());

        // Spawn walls.
        FXGL.spawn("wallLeft");
        FXGL.spawn("wallRight");
        FXGL.spawn("wallTop");

        // Spawn paddle
        double px = FXGL.getAppWidth() / 2.0 - 60;
        double py = FXGL.getAppHeight() - 40;
        FXGL.spawn("paddle", px, py);

        FXGL.spawn("normalBrick", 100, 100);
        FXGL.spawn("normalBrick", 200, 200);
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onActionBegin() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(e -> e.getComponent(Move.class).left());
            }

            @Override
            protected void onActionEnd() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(e -> e.getComponent(Move.class).stop());
            }
        }, KeyCode.LEFT);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onActionBegin() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(e -> e.getComponent(Move.class).right());
            }

            @Override
            protected void onActionEnd() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(e -> e.getComponent(Move.class).stop());
            }
        }, KeyCode.RIGHT);
    }

    @Override
    protected void initPhysics() {
        CollisionSystem.getInstance().apply();
    }

    @Override
    protected void initUI() {
        FXGL.getGameScene().setBackgroundColor(Color.web("#2B2B2B"));
    }

    public static void main(String[] args) {
        launchOption = new LaunchOption(args);
        launch(args);
    }
}
