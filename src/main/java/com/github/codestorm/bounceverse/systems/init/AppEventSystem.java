package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.systems.manager.metrics.LeaderboardManager;
import com.github.codestorm.bounceverse.systems.manager.settings.UserSettingsManager;

/**
 *
 *
 * <h1>{@link AppEventSystem}</h1>
 *
 * {@link InitialSystem} quản lý các sự kiện trên {@link GameApplication} <br>
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 */
public final class AppEventSystem extends InitialSystem {
    private AppEventSystem() {}

    public static AppEventSystem getInstance() {
        return Holder.INSTANCE;
    }

    private void onStart() {
        // User Settings
        UserSettingsManager.getInstance().apply();

        // Save load
        FXGL.getSaveLoadService().addHandler(GameSystem.Variables.SAVE_LOAD_HANDLER);
    }

    private void onExit() {
        // Settings
        UserSettingsManager.getInstance().sync();
        UserSettingsManager.getInstance().save();

        // Leaderboard
        LeaderboardManager.getInstance().save();
    }

    @Override
    public void apply() {
        onStart();
        Runtime.getRuntime().addShutdownHook(new Thread(this::onExit));
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final AppEventSystem INSTANCE = new AppEventSystem();
    }
}
