package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;

public final class UISystem extends System {
    public static UISystem getInstance() {
        return UISystem.Holder.INSTANCE;
    }

    @Override
    public void apply() {
        FXGL.getGameScene().setBackgroundColor(Color.web("#2B2B2B"));
    }

    /**
     * Lazy-loaded singleton holder.
     *
     * <p>Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final UISystem INSTANCE = new UISystem();
    }
}
