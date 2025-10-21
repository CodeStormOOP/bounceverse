package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.scene.Scene;
import com.github.codestorm.bounceverse.scenes.MainMenu;
import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * <h1>{@link SceneFactory}</h1>
 *
 * Nơi sản xuất các {@link Scene} cho trò chơi.
 *
 * @see com.almasb.fxgl.app.scene.SceneFactory
 */
public class SceneFactory extends com.almasb.fxgl.app.scene.SceneFactory {
    @NotNull @Override
    public FXGLMenu newMainMenu() {
        return new MainMenu();
    }
}
