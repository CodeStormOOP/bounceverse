package com.github.codestorm.bounceverse;

/**
 *
 *
 * <h1>{@link Assets}</h1>
 *
 * Nơi lưu trữ các đường dẫn tới Assets.
 */
public final class Assets {
    private Assets() {}

    private static final String ROOT = "/assets";

    public static final class Video {
        private Video() {}

        private static final String ROOT = Assets.ROOT + "/video";

        public static final String INTRO = ROOT + "/intro.mp4";
    }
}
