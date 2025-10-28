package com.github.codestorm.bounceverse.core;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameSettings;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.factory.SceneFactory;
import java.io.IOException;

/**
 *
 *
 * <h1>{@link SettingsManager}</h1>
 *
 * Nơi quản lý các thiết lập trong game.
 *
 * @see GameSettings
 */
public final class SettingsManager {
    private SettingsManager() {}

    /**
     * Tải các settings từ file đã thiết lập vào CTDL.
     *
     * <h2>Chú ý:
     *
     * <ul>
     *   <li>Cần load {@link LaunchOptions#load(String...)} trước khi tải.
     * </ul>
     *
     * @param target Nơi tải vào
     * @throws IOException if an error occurred when reading from the input stream.
     */
    public static void load(GameSettings target) throws IOException {
        final var gameSettings = Utilities.IO.loadProperties("/settings.properties");
        final var userSettings = UserSetting.getSetting();

        // ? General
        target.setTitle(gameSettings.getProperty("general.name"));
        target.setVersion(gameSettings.getProperty("general.version"));
        target.setCredits(Utilities.IO.readTextFile("credits.txt"));
        target.setApplicationMode(
                Boolean.parseBoolean(gameSettings.getProperty("general.devMode"))
                        ? ApplicationMode.DEVELOPER
                        : (LaunchOptions.isDebug())
                                ? ApplicationMode.DEBUG
                                : ApplicationMode.RELEASE);

        // ? Display
        target.setWidth(userSettings.getVideo().getWidth());
        target.setHeight(userSettings.getVideo().getHeight());
        target.setFullScreenAllowed(true);

        // ? In-game
        target.setSceneFactory(new SceneFactory());
        target.setMainMenuEnabled(true);
        target.setIntroEnabled(true);
    }
}
