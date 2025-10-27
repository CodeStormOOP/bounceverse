package com.github.codestorm.bounceverse.core;

import com.almasb.fxgl.logging.Logger;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 *
 * <h1>{@link UserSetting}</h1>
 *
 * Thiết lập game của người chơi.
 */
public final class UserSetting {
    private static final String FORMAT = "toml";
    private Video video = new Video();

    private UserSetting() {}

    /**
     * Lấy tên file setting tương ứng của người chơi.
     *
     * @return Tên file
     */
    public static String getFilename() {
        final String username = System.getProperty("user.name");
        return String.format("config.%s.%s", username, FORMAT);
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
     * Lấy thiết lập game của người chơi.
     *
     * @return Thiết lập game của người chơi
     */
    public static UserSetting getSetting() {
        final var filepath = getFilepath();
        try {
            final var file = new Toml().read(filepath.toString());
            return file.to(UserSetting.class);
        } catch (IllegalStateException e) {
            Logger.get(UserSetting.class)
                    .warning(
                            "Using default config because the user config file cannot be opened: "
                                    + filepath,
                            e);
            return new UserSetting();
        }
    }

    /**
     * Lưu thiết lập game thành file.
     *
     * @throws IOException if any file operations fail
     */
    public void save() throws IOException {
        final var filepath = getFilepath();
        TomlWriter writer = new TomlWriter();
        writer.write(this, new File(filepath.toUri()));
        Logger.get(UserSetting.class).infof("Saved user config to: %s", filepath);
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    /** Setting về Hình ảnh. */
    public static final class Video {
        private int width = 1024;
        private int height = 768;

        private Video() {}

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
