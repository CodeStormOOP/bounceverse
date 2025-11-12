package com.github.codestorm.bounceverse.scenes;

import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.logging.Logger;
import com.github.codestorm.bounceverse.AssetsPath;

import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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

        try {
            // Tải video
            final var video = FXGL.getAssetLoader().loadVideo(AssetsPath.Video.INTRO);
            video.setFitWidth(FXGL.getAppWidth());
            video.setFitHeight(FXGL.getAppHeight());
            addChild(video);

            final var player = video.getMediaPlayer();

            // Xử lý khi video kết thúc thành công
            player.setOnEndOfMedia(
                    () -> {
                        player.dispose();
                        finishIntro();
                    });

            // SỬA ĐỔI: Xử lý khi có lỗi xảy ra
            player.setOnError(
                    () -> {
                        Logger.get(Intro.class).fatal("Cannot play intro video", player.getError());

                        // Hiển thị thông báo lỗi thay vì bỏ qua ngay lập tức
                        showErrorAndExit();

                        player.dispose(); // Dọn dẹp media player
                    });

            // Bỏ qua intro khi người dùng click chuột
            getContentRoot()
                    .setOnMouseClicked(
                            event -> {
                                if (event.getButton() == MouseButton.PRIMARY) {
                                    player.getOnEndOfMedia().run();
                                }
                            });

            // Bắt đầu phát khi video đã sẵn sàng
            player.setOnReady(player::play);

        } catch (Exception e) {
            // Bắt lỗi nếu ngay cả việc tải video ban đầu cũng thất bại (ví dụ file không tồn tại)
            Logger.get(Intro.class).fatal("Failed to load intro video asset.", e);
        }
    }

    /** Hiển thị một thông báo lỗi trên màn hình trong 3 giây rồi kết thúc intro. */
    private void showErrorAndExit() {
        // Xóa các thành phần cũ (nếu có)
        getContentRoot().getChildren().clear();

        // Tạo văn bản thông báo lỗi
        var errorText =
                FXGL.getUIFactoryService()
                        .newText(
                                "Lỗi: Không thể tải video intro.\n"
                                        + "Vui lòng kiểm tra file tại\n"
                                        + "src/main/resources/assets/videos/intro.mp4",
                                Color.WHITE,
                                22.0);
        errorText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Đặt văn bản vào giữa màn hình
        var layout = new StackPane(errorText);
        layout.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());
        StackPane.setAlignment(errorText, Pos.CENTER);

        addChild(layout);

        // Tự động kết thúc intro sau 3.5 giây để người dùng kịp đọc
        FXGL.getGameTimer().runOnceAfter(this::finishIntro, Duration.seconds(3.5));
    }
}
