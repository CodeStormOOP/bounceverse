package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.gameManager.GameManager;
import javafx.scene.paint.Color;

public class BounceVerseApp extends GameApplication {
    private GameManager gameManager;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(900);
        settings.setHeight(600);
        settings.setTitle("");
    }

    @Override
    protected void initGame() {
        gameManager = new GameManager();

        FXGL.getGameScene().setBackgroundColor(Color.web("#2B2B2B"));
        gameManager.spawnBricks();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
