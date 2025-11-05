package com.github.codestorm.bounceverse.core.settings;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.logging.Logger;
import com.github.codestorm.bounceverse.typing.records.UserSettings;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 *
 * <h1>{@link UserSettingsManager}</h1>
 *
 * Quản lý {@link UserSettings}.
 *
 * @see GameSettingsManager
 */
public final class UserSettingsManager extends SettingsManager {
    private static final String FORMAT = "toml";

    private UserSettingsManager() {}

    /**
     * Lấy tên file setting tương ứng của người chơi.
     *
     * @return Tên file
     */
    public static String getSettingsFilename() {
        final String username = System.getProperty("user.name");
        return String.format("settings.%s.%s", username, FORMAT);
    }

    /**
     * Lấy địa chỉ file settings tương ứng của người chơi.
     *
     * @return Địa chỉ file setting tuyệt đối
     */
    public static Path getSettingsFilepath() {
        return Paths.get(getSettingsFilename()).toAbsolutePath();
    }

    /**
     * Load setting được chỉ định.
     *
     * <p><b>Chú ý: Cần loadSettings {@link GameSettingsManager#loadSettings(GameSettings)} trước
     * khi tải.</b>
     *
     * @param settings Setting muốn loadSettings
     */
    public static void loadSettings(UserSettings settings) {
        // ? Video
        FXGL.getPrimaryStage().setWidth(settings.getVideo().getWidth());
        FXGL.getPrimaryStage().setHeight(settings.getVideo().getHeight());
        FXGL.getPrimaryStage().setFullScreen(settings.getVideo().isFullscreen());

        // ? Audio
        FXGL.getSettings().setGlobalMusicVolume(settings.getAudio().getMusic());
        FXGL.getSettings().setGlobalSoundVolume(settings.getAudio().getSound());
    }

    /**
     * Load settings của người dùng từ file {@link #getSettingsFilepath()}.
     *
     * <p><b>Chú ý: Cần loadSettings {@link GameSettingsManager#loadSettings(GameSettings)} trước
     * khi tải.</b>
     */
    public static void loadSettings() {
        final var filepath = getSettingsFilepath();
        try {
            final var file = new File(filepath.toUri());
            final var parsed = new Toml().read(file);
            UserSettings settings = parsed.to(UserSettings.class);
            Logger.get(UserSettingsManager.class).infof("Read user settings in: %s", filepath);
            loadSettings(settings);
            return;
        } catch (Exception e) {
            Logger.get(UserSettingsManager.class)
                    .warning("Cannot read user settings in: " + filepath, e);
        }
        Logger.get(UserSettingsManager.class).info("Using default user settings");
        loadSettings(new UserSettings());
    }

    /**
     * Lấy Settings người dùng.
     *
     * @return Setting người dùng
     */
    public static UserSettings getSettings() {
        final var settings = new UserSettings();

        // ? Video
        settings.getVideo().setWidth(FXGL.getPrimaryStage().getWidth());
        settings.getVideo().setHeight(FXGL.getPrimaryStage().getHeight());
        settings.getVideo().setFullscreen(FXGL.getPrimaryStage().isFullScreen());

        // ? Audio
        settings.getAudio().setMusic(FXGL.getSettings().getGlobalMusicVolume());
        settings.getAudio().setSound(FXGL.getSettings().getGlobalSoundVolume());

        return settings;
    }

    /** Lưu thiết lập game tại {@link #getSettingsFilepath()}. */
    public static void saveSettings() {
        final var filepath = getSettingsFilepath();
        TomlWriter writer = new TomlWriter();
        try {
            writer.write(getSettings(), new File(filepath.toUri()));
            Logger.get(UserSettingsManager.class).infof("Saved user settings to: %s", filepath);
        } catch (IOException e) {
            Logger.get(UserSettingsManager.class)
                    .fatal("Cannot saveSettings user settings to: " + filepath, e);
        }
    }
}
