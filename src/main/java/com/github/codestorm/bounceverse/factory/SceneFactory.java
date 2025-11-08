package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.scene.Scene;
import com.github.codestorm.bounceverse.scenes.Intro;
import com.github.codestorm.bounceverse.scenes.Menu;

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
public final class SceneFactory extends com.almasb.fxgl.app.scene.SceneFactory {
    @NotNull @Override
    public FXGLMenu newMainMenu() {
        return new Menu(MenuType.MAIN_MENU);
    }

    @NotNull @Override
    public IntroScene newIntro() {
        return new Intro();
    }
}
