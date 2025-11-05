package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.app.GameApplication;
import com.github.codestorm.bounceverse.core.settings.UserSettingsManager;

/**
 *
 *
 * <h1>{@link AppEventSystem}</h1>
 *
 * {@link System} quản lý các sự kiện trên {@link GameApplication} <br>
 * <i>Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}</i>.
 */
public final class AppEventSystem extends System {
    private AppEventSystem() {}

    public static AppEventSystem getInstance() {
        return Holder.INSTANCE;
    }

    private void onStart() {
        UserSettingsManager.load();
    }

    private void onExit() {
        UserSettingsManager.save();
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
