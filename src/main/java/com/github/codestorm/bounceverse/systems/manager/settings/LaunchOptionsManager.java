package com.github.codestorm.bounceverse.systems.manager.settings;

import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.typing.records.LaunchOptions;

/**
 *
 *
 * <h1>{@link LaunchOptionsManager}</h1>
 *
 * Các tùy chọn khi khởi động được áp dụng trong game. <br>
 * Sử dụng {@link #load(String...)} để tải các options. <br>
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 */
public final class LaunchOptionsManager extends SettingsManager {
    public static LaunchOptionsManager getInstance() {
        return Holder.INSTANCE;
    }

    private LaunchOptions options;

    private LaunchOptionsManager() {}

    /**
     * Tải các launch options từ Command-line Arguments.
     *
     * @param args Command-Line Arguments - được truyền vào từ hàm {@code main()}
     */
    public void load(String... args) {
        final var map = Utilities.IO.parseArgs(args, null, null);

        options = new LaunchOptions(Boolean.parseBoolean(map.getOrDefault("debug", "false")));
    }

    public LaunchOptions get() {
        return options;
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final LaunchOptionsManager INSTANCE = new LaunchOptionsManager();
    }
}
