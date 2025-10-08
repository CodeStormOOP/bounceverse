package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.factory.BrickFactory;
import javafx.scene.paint.Color;

public final class Bounceverse extends GameApplication {
    public static final String name = "Bounceverse";

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(900);
        settings.setHeight(600);
        settings.setTitle(name);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BrickFactory());

        var brick1 = FXGL.spawn("normalBrick", 100, 100);
        var brick2 = FXGL.spawn("normalBrick", 100, 100);
    }

    @Override
    protected void initUI() {
        FXGL.getGameScene().setBackgroundColor(Color.web("#2B2B2B"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
