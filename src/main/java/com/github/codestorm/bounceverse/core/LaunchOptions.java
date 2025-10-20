package com.github.codestorm.bounceverse.core;

import com.github.codestorm.bounceverse.Utils;

/**
 *
 *
 * <h1>{@link LaunchOptions}</h1>
 *
 * Các tùy chọn khi khởi động được áp dụng trong game.
 *
 * <p>Sử dụng {@link #load(String...)} để tải các options.
 */
public final class LaunchOptions {
    private static boolean debug = false;

    private LaunchOptions() {}

    /**
     * Tải các launch options từ Command-line Arguments.
     *
     * @param args Command-Line Arguments - được truyền vào từ hàm {@code main()}
     */
    public static void load(String... args) {
        final var map = Utils.IO.parseArgs(args, null, null);

        debug = Boolean.parseBoolean(map.getOrDefault("debug", "false"));
    }

    public static boolean isDebug() {
        return debug;
    }
}
