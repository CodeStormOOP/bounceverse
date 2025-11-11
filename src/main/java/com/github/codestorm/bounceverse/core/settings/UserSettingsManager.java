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
 * <h1>{@link UserSettingsManager}</h1>
 *
 * Quản lý {@link UserSettings}.
 *
 * @see GameSettingsManager
 */
public final class UserSettingsManager {

    private static final String FORMAT = "toml";

    private UserSettingsManager() {
    }

    /**
     * Lấy tên file setting tương ứng của người chơi.
     *
     * @return Tên file
     */
    public static String getFilename() {
        final String username = System.getProperty("user.name");
        return String.format("settings.%s.%s", username, FORMAT);
    }

    /**
     * Lấy địa chỉ file setting tương ứng của người chơi.
     *
     * @return Địa chỉ file setting tuyệt đối
     */
    public static Path getFilepath() {
        return Paths.get(getFilename()).toAbsolutePath();
    }

    /**
     * Load setting được chỉ định.
     *
     * <p>
     * <b>Chú ý:</b> Cần loadTo {@link GameSettingsManager#loadTo(GameSettings)}
     * trước khi tải.
     *
     * @param settings Setting muốn loadTo
     */
    public static void load(UserSettings settings) {
        // Video
        FXGL.getPrimaryStage().setWidth(settings.getVideo().getWidth());
        FXGL.getPrimaryStage().setHeight(settings.getVideo().getHeight());
        FXGL.getPrimaryStage().setFullScreen(settings.getVideo().isFullscreen());

        // Audio
        FXGL.getSettings().setGlobalMusicVolume(settings.getAudio().getMusic());
        FXGL.getSettings().setGlobalSoundVolume(settings.getAudio().getSound());
    }

    /**
     * Load settings của người dùng từ file {@link #getFilepath()}.
     */
    public static void load() {
        final var filepath = getFilepath();
        try {
            final var file = new File(filepath.toUri());
            final var parsed = new Toml().read(file);
            UserSettings settings = parsed.to(UserSettings.class);
            Logger.get(UserSettingsManager.class).infof("Read user settings in: %s", filepath);
            load(settings);
            return;
        } catch (Exception e) {
            Logger.get(UserSettingsManager.class)
                    .warning("Cannot read user settings in: " + filepath, e);
        }

        Logger.get(UserSettingsManager.class).info("Using default user settings");
        load(new UserSettings());
    }

    /**
     * Lấy Settings người dùng.
     *
     * @return Setting người dùng
     */
    public static UserSettings get() {
        final var settings = new UserSettings();

        // Video
        settings.getVideo().setWidth(FXGL.getPrimaryStage().getWidth());
        settings.getVideo().setHeight(FXGL.getPrimaryStage().getHeight());
        settings.getVideo().setFullscreen(FXGL.getPrimaryStage().isFullScreen());

        // Audio
        settings.getAudio().setMusic(FXGL.getSettings().getGlobalMusicVolume());
        settings.getAudio().setSound(FXGL.getSettings().getGlobalSoundVolume());

        return settings;
    }

    /** Lưu thiết lập game tại {@link #getFilepath()}. */
    public static void save() {
        final var filepath = getFilepath();
        TomlWriter writer = new TomlWriter();
        try {
            writer.write(get(), new File(filepath.toUri()));
            Logger.get(UserSettingsManager.class).infof("Saved user settings to: %s", filepath);
        } catch (IOException e) {
            Logger.get(UserSettingsManager.class)
                    .fatal("Cannot save user settings to: " + filepath, e);
        }
    }

    private static final UserSettingsManager INSTANCE = new UserSettingsManager();

    public static UserSettingsManager getInstance() {
        return INSTANCE;
    }

    /** Áp dụng cài đặt người dùng (tải từ file và apply ngay). */
    public void apply() {
        load();
    }
}
