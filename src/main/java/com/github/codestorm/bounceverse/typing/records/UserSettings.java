package com.github.codestorm.bounceverse.typing.records;

import com.github.codestorm.bounceverse.core.settings.UserSettingsManager;

/**
 *
 *
 * <h1>{@link UserSettings}</h1>
 *
 * Biểu diễn Settings của người dùng.
 *
 * @see UserSettingsManager
 */
public final class UserSettings {
    private final Video video = new Video();
    private final Audio audio = new Audio();

    public Video getVideo() {
        return video;
    }

    public Audio getAudio() {
        return audio;
    }

    /** Setting về Hình ảnh. */
    public static final class Video {
        private double width = 1280;
        private double height = 960;
        private boolean fullscreen = false;

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public boolean isFullscreen() {
            return fullscreen;
        }

        public void setFullscreen(boolean fullscreen) {
            this.fullscreen = fullscreen;
        }
    }

    /** Setting về Âm thanh */
    public static final class Audio {
        private double music = 1;
        private double sound = 1;

        public double getMusic() {
            return music;
        }

        public void setMusic(double music) {
            this.music = music;
        }

        public double getSound() {
            return sound;
        }

        public void setSound(double sound) {
            this.sound = sound;
        }
    }
}
