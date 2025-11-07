package com.github.codestorm.bounceverse.scenes;

import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.logging.Logger;
import com.github.codestorm.bounceverse.Assets;
import javafx.scene.input.MouseButton;
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
public final class Intro extends IntroScene {
    @Override
    public void startIntro() {
        // Background
        setBackgroundColor(Color.BLACK);
        setCursorInvisible();

        // Video
        final var path = FXGL.getAssetLoader().getURL(Assets.Video.INTRO);
        final var view = FXGL.getAssetLoader().loadVideo(path);
        final var player = view.getMediaPlayer();
        final var media = player.getMedia();

        getContentRoot().getChildren().add(view);

        player.setAutoPlay(true);
        player.setOnEndOfMedia(
                () -> {
                    player.dispose();
                    finishIntro();
                });
        media.setOnError(
                () -> {
                    Logger.get(Intro.class).fatalf("Cannot load video: %s", media.getError());
                    player.getOnEndOfMedia().run();
                });
        player.setOnError(
                () -> {
                    Logger.get(Intro.class).fatalf("Cannot play video: %s", player.getError());
                    player.getOnEndOfMedia().run();
                });
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

        getContentRoot()
                .setOnMouseClicked(
                        event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                player.getOnEndOfMedia().run();
                            }
                        });
    }
}
