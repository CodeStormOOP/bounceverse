package com.github.codestorm.bounceverse.scenes;

import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.logging.Logger;
import com.github.codestorm.bounceverse.AssetsPath;

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
        final var video = FXGL.getAssetLoader().loadVideo(AssetsPath.Video.INTRO);
        video.setFitWidth(FXGL.getAppWidth());
        video.setFitHeight(FXGL.getAppHeight());
        addChild(video);

        final var player = video.getMediaPlayer();
        player.setOnEndOfMedia(
                () -> {
                    player.dispose();
                    finishIntro();
                });
        player.setOnError(
                () -> {
                    Logger.get(Intro.class).fatal("Cannot play intro video", player.getError());
                    player.getOnEndOfMedia().run();
                });

        getContentRoot()
                .setOnMouseClicked(
                        event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                player.getOnEndOfMedia().run();
                            }
                        });

        player.setOnReady(player::play);
    }
}
