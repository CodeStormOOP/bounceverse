package com.github.codestorm.bounceverse.systems.manager.settings;

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
 * Quản lý {@link UserSettings}. <br>
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 * @see GameSettingsManager
 */
public final class UserSettingsManager extends SettingsManager {
    private static final String FORMAT = "toml";

    public static UserSettingsManager getInstance() {
        return Holder.INSTANCE;
    }

    private UserSettings profile;

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
     * Áp dụng setting {@link #profile} mà đã được lưu vào bộ nhớ.
     *
     * @apiNote Chỉ thực thi khi {@link FXGL} có thể sử dụng an toàn
     */
    public void apply() {
        // ? Video
        FXGL.getPrimaryStage().setFullScreen(profile.getVideo().isFullscreen());

        // ? Audio
        FXGL.getSettings().setGlobalMusicVolume(profile.getAudio().getMusic());
        FXGL.getSettings().setGlobalSoundVolume(profile.getAudio().getSound());
    }

    /**
     * Load settings của file {@link #getSettingsFilepath()} vào bộ nhớ {@link #profile}.
     *
     * @apiNote Chỉ load vào bộ nhớ, không áp dụng. Hãy dùng {@link #apply()}.
     */
    public void load() {
        final var filepath = getSettingsFilepath();
        try {
            final var file = new File(filepath.toUri());
            final var parsed = new Toml().read(file);
            profile = parsed.to(UserSettings.class);
            Logger.get(UserSettingsManager.class).infof("Read user settings in: %s", filepath);
            return;
        } catch (Exception e) {
            Logger.get(UserSettingsManager.class)
                    .warning("Cannot read user settings in: " + filepath, e);
        }
        Logger.get(UserSettingsManager.class).info("Using default user settings");
        profile = new UserSettings();
    }

    /**
     * Cập nhật {@link #profile} trong instance với giá trị thực tế.
     *
     * @apiNote Chỉ thực thi khi {@link FXGL} có thể sử dụng an toàn
     */
    public void sync() {
        // ? Video
        profile.getVideo().setFullscreen(FXGL.getSettings().getFullScreen().get());

        // ? Audio
        profile.getAudio().setMusic(FXGL.getSettings().getGlobalMusicVolume());
        profile.getAudio().setSound(FXGL.getSettings().getGlobalSoundVolume());
    }

    /**
     * Lấy Settings người dùng {@link #profile} trong bộ nhớ.
     *
     * @apiNote Sử dụng {@link #sync()} để cập nhật settings với giá trị thực tế.
     * @return Settings người dùng
     */
    public UserSettings get() {
        return profile;
    }

    /**
     * Lưu thiết lập game tại {@link #getSettingsFilepath()}.
     *
     * @apiNote Cập nhật settings đến thời điểm mới nhất bằng {@link #sync()}
     */
    public void save() {
        final var filepath = getSettingsFilepath();
        final var file = new File(filepath.toUri());
        final var writer = new TomlWriter();
        try {
            writer.write(profile, file);
            Logger.get(UserSettingsManager.class).infof("Saved user settings to: %s", filepath);
        } catch (IOException e) {
            Logger.get(UserSettingsManager.class)
                    .fatal("Cannot save user settings to: " + filepath, e);
        }
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final UserSettingsManager INSTANCE = new UserSettingsManager();
    }
}
