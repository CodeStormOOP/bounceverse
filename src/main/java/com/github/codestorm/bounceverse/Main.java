package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.factory.BallFactory;

public class Main extends GameApplication {

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Bounceverse");
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BallFactory());
        FXGL.spawn("ball");
    }

    static void main(String[] args) {
        launch(args);
    }
}
