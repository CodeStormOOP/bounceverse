package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.factory.BallFactory;
import com.github.codestorm.bounceverse.factory.BrickFactory;
import com.github.codestorm.bounceverse.factory.SceneFactory;
import com.github.codestorm.bounceverse.factory.WallFactory;
import com.github.codestorm.bounceverse.systems.LaunchOption;
import com.github.codestorm.bounceverse.systems.physics.CollisionSystem;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.paint.Color;

/**
 *
 *
 * <h1>{@link Bounceverse}</h1>
 *
 * Phần Hệ thống Chương trình chính của game, nơi mà mọi thứ bắt đầu...
 *
 * <p><i>Game {@link Bounceverse} được lấy cảm hứng từ game Arkanoid nổi tiếng, nơi người chơi điều
 * khiển một thanh để đỡ bóng và phá vỡ các viên gạch. Mục tiêu của game là phá vỡ tất cả các viên
 * gạch và dành được điểm số cao nhất. Nhưng liệu mọi thứ chỉ đơn giản như vậy?</i>
 */
public final class Bounceverse extends GameApplication {
    private static LaunchOption launchOption;

    /**
     * Cấu hình game.
     *
     * <p>Sử dụng {@link #loadConfigs()} để load các config.
     */
    private static final class Configs {
        private static final String ROOT = "/configs/";

        /** Cấu hình game bên trong hệ thống game. */
        private static final class System {
            public static Properties settings;

            private System() {}
        }

        /** Cấu hình game bên ngoài hệ thống game. */
        private static final class Options {
            public static Properties DEFAULT; // Cấu hình mặc định của trò chơi

            private Options() {}
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

        private Configs() {}
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
        FXGL.getGameWorld().addEntityFactory(new BallFactory());
        FXGL.getGameWorld().addEntityFactory(new WallFactory());

        WallFactory.spawnWalls();

        //        var brick1 = FXGL.spawn("normalBrick", 100, 100);
        //        var brick2 = FXGL.spawn("normalBrick", 200, 200);

        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 10; j++) {
                FXGL.spawn("normalBrick", 85 * j, 35 * i);
            }
        }
        FXGL.spawn("ball");
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);
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
