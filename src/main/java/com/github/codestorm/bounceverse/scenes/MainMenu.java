package com.github.codestorm.bounceverse.scenes;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.ui.FXGLScrollPane;

/**
 *
 *
 * <h1>{@link MainMenu}</h1>
 *
 * {@link FXGLMenu} loại {@link MenuType#MAIN_MENU} được sử dụng trong trò chơi.
 */
public final class MainMenu extends FXGLDefaultMenu {
    public MainMenu() {
        super(MenuType.MAIN_MENU);
        // TODO: Customize Main Menu

    }

    private MenuContent createContentLeaderboard() {
        final var log = Logger.get(MainMenu.class);
        log.debug("createContentCredits()");

        final var pane = new FXGLScrollPane();
        pane.setPrefWidth(500.0);
        pane.setPrefHeight((double) getAppHeight() / 2);
        pane.setStyle("-fx-background:black;");

        //        final var vbox = new VBox();
        //        vbox.setAlignment(Pos.CENTER);
        //        vbox.setPrefWidth(pane.getPrefWidth() - 15);
        //
        //        val credits = ArrayList(getSettings().credits)
        //        credits.add("")
        //        credits.add("Powered by FXGL " + FXGL.getVersion())
        //        credits.add("Author: Almas Baimagambetov")
        //        credits.add("https://github.com/AlmasB/FXGL")
        //        credits.add("")
        //
        //        for (credit in credits) {
        //            if (credit.length > 45) {
        //                log.warning("Credit name length > 45: $credit")
        //            }
        //
        //            vbox.children.add(getUIFactoryService().newText(credit))
        //        }
        //
        //        pane.content = vbox

        return new MenuContent(pane);
    }
}
