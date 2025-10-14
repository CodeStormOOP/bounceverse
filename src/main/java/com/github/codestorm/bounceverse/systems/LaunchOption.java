package com.github.codestorm.bounceverse.systems;

import com.github.codestorm.bounceverse.Utils;

/**
 *
 *
 * <h1>{@link LaunchOption}</h1>
 *
 * Các tùy chọn khởi động được áp dụng trong game.
 */
public final class LaunchOption {
    private boolean debug = false;

    public boolean isDebug() {
        return debug;
    }

    public LaunchOption(String... args) {
        var map = Utils.IO.parseArgs(args, null, null);
        debug = Boolean.parseBoolean(map.getOrDefault("debug", "false"));
    }
}
