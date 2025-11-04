package com.github.codestorm.bounceverse.core.settings;

import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.typing.records.LaunchOptions;

/**
 *
 *
 * <h1>{@link LaunchOptionsManager}</h1>
 *
 * Các tùy chọn khi khởi động được áp dụng trong game. <br>
 * Sử dụng {@link #load(String...)} để tải các options.
 */
public final class LaunchOptionsManager {
    private static LaunchOptions options;

    private LaunchOptionsManager() {}

    /**
     * Tải các launch options từ Command-line Arguments.
     *
     * @param args Command-Line Arguments - được truyền vào từ hàm {@code main()}
     */
    public static void load(String... args) {
        final var map = Utilities.IO.parseArgs(args, null, null);

        options = new LaunchOptions(Boolean.parseBoolean(map.getOrDefault("debug", "false")));
    }

    public static LaunchOptions getOptions() {
        return options;
    }
}
