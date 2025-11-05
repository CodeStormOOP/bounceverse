package com.github.codestorm.bounceverse.scenes;

import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.Assets;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

/**
 *
 *
 * <h1>{@link Intro}</h1>
 *
 * Intro của game.
 *
 * @see IntroScene
 */
public class Intro extends IntroScene {
    @Override
    public void startIntro() {
        // Background
        setBackgroundColor(Color.BLACK);
        setCursorInvisible();

        // Video
        final var path = FXGL.getAssetLoader().getURL(Assets.Video.INTRO).toExternalForm();
        final var media = new Media(path);
        final var player = new MediaPlayer(media);
        final var view = new MediaView(player);

        player.setAutoPlay(true);
        getContentRoot().getChildren().add(view);

        player.setOnReady(
                () -> {
                    final var screenW = getAppWidth();
                    final var screenH = getAppHeight();
                    final var videoW = media.getWidth();
                    final var videoH = media.getHeight();

                    double fitW = videoW * ((double) screenW / videoW);
                    double fitH = videoH * ((double) screenH / videoH);

                    view.setFitWidth(fitW);
                    view.setFitHeight(fitH);
                    view.setX((screenW - fitW) / 2);
                    view.setY((screenH - fitH) / 2);
                });

        player.setOnEndOfMedia(
                () -> {
                    player.dispose();
                    finishIntro();
                });
    }
}
