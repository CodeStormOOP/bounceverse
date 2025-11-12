package com.github.codestorm.bounceverse.typing.records;

import com.github.codestorm.bounceverse.systems.manager.settings.UserSettingsManager;

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
    private final Controls controls = new Controls();

    public Video getVideo() {
        return video;
    }

    public Audio getAudio() {
        return audio;
    }

    public Controls getControls() {
        return controls;
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

    /** Setting về Controls/Phím điều khiển */
    public static final class Controls {
        private String moveLeft = "LEFT";
        private String moveRight = "RIGHT";
        private String launchBall = "SPACE";

        public String getMoveLeft() {
            return moveLeft;
        }

        public void setMoveLeft(String moveLeft) {
            this.moveLeft = moveLeft;
        }

        public String getMoveRight() {
            return moveRight;
        }

        public void setMoveRight(String moveRight) {
            this.moveRight = moveRight;
        }

        public String getLaunchBall() {
            return launchBall;
        }

        public void setLaunchBall(String launchBall) {
            this.launchBall = launchBall;
        }
    }
}
