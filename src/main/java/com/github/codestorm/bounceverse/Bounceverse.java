package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.factory.BrickFactory;
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
    private static String[] args; // launch options
    private static boolean debug = false; // debug mode is enabled

    /**
     * Cấu hình game.
     *
     * <p>Sử dụng {@link Configs#loadConfigs()} để load các config.
     */
    private static final class Configs {
        private static final String ROOT = "/configs/";

        /** Cấu hình game bên ngoài hệ thống game. */
        private static final class Options {
            public static Properties DEFAULT; // Cấu hình mặc định của trò chơi

            private Options() {}
        }

        /** Cấu hình game bên trong hệ thống game. */
        private static final class System {
            public static Properties settings;

            private System() {}
        }

        /**
         * Load game configs.
         *
         * @throws IOException if an error occurred when reading from the input stream.
         */
        public static void loadConfigs() throws IOException {
            // TODO: Load args
            Options.DEFAULT = Utils.File.loadProperties(ROOT + "default.properties");
            System.settings = Utils.File.loadProperties(ROOT + "system/settings.properties");
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
        settings.setApplicationMode(
                Boolean.parseBoolean(Configs.System.settings.getProperty("settings.devMode"))
                        ? ApplicationMode.DEVELOPER
                        : (debug) ? ApplicationMode.DEBUG : ApplicationMode.RELEASE);
        // Resolution
        settings.setWidth(Integer.parseInt(Configs.Options.DEFAULT.getProperty("width")));
        settings.setHeight(Integer.parseInt(Configs.Options.DEFAULT.getProperty("height")));
        settings.setPreserveResizeRatio(true);
        settings.setManualResizeEnabled(true);
        // In-game
        settings.setMainMenuEnabled(true);
        settings.setIntroEnabled(true); // TODO: Video cho intro
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BrickFactory());

        var brick1 = FXGL.spawn("normalBrick", 100, 100);
        var brick2 = FXGL.spawn("normalBrick", 200, 200);
    }

    @Override
    protected void initUI() {
        FXGL.getGameScene().setBackgroundColor(Color.web("#2B2B2B"));
    }

    public static void main(String[] args) {
        launch(Bounceverse.args = args);
    }
}
