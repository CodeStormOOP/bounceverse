package com.github.codestorm.bounceverse.core.settings;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.ReadOnlyGameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.factory.SceneFactory;
import java.io.IOException;

/**
 *
 *
 * <h1>{@link GameSettingsManager}</h1>
 *
 * Trình quản lý việc loadSettings {@link GameSettings}. <br>
 *
 * @see UserSettingsManager
 */
public final class GameSettingsManager extends SettingsManager {
    private GameSettingsManager() {}

    /**
     * Tải các settings từ file đã thiết lập vào CTDL. <br>
     * <b>Chú ý: Cần loadSettings {@link LaunchOptionsManager#loadSettings(String...)} trước khi
     * tải.</b>
     *
     * @param settings Nơi tải vào
     * @throws IOException if an error occurred when reading from the input stream.
     */
    public static void loadSettings(GameSettings settings) throws IOException {
        final var gameSettings = Utilities.IO.loadProperties("/settings.properties");

        // ? General
        settings.setTitle(gameSettings.getProperty("general.name"));
        settings.setVersion(gameSettings.getProperty("general.version"));
        settings.setCredits(Utilities.IO.readTextFile("credits.txt"));
        settings.setApplicationMode(
                Boolean.parseBoolean(gameSettings.getProperty("general.devMode"))
                        ? ApplicationMode.DEVELOPER
                        : (LaunchOptionsManager.getInstance().getSettings().debug())
                                ? ApplicationMode.DEBUG
                                : ApplicationMode.RELEASE);

        // ? Display
        settings.setWidth(Integer.parseInt(gameSettings.getProperty("logic.width")));
        settings.setHeight(Integer.parseInt(gameSettings.getProperty("logic.height")));
        settings.setFullScreenAllowed(true);

        // ? In-game
        settings.setSceneFactory(new SceneFactory());
        settings.setMainMenuEnabled(true);
        settings.setIntroEnabled(true);
    }

    /**
     * Lây settings trong game.
     *
     * @return Settings trong game (Read-only)
     */
    public static ReadOnlyGameSettings getSettings() {
        return FXGL.getSettings();
    }
}
