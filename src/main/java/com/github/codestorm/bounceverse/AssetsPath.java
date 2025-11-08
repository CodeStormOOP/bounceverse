package com.github.codestorm.bounceverse;

/**
 *
 *
 * <h1>{@link AssetsPath}</h1>
 *
 * Nơi lưu trữ các đường dẫn tới AssetsPath.
 */
public final class AssetsPath {
    private AssetsPath() {}

    private static final String ROOT = "/assets";

    public static final class Video {
        private Video() {}

        private static final String ROOT = AssetsPath.ROOT + "/videos";

        public static final String INTRO = "intro.mp4";
    }
}
