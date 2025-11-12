package com.github.codestorm.bounceverse.scenes.subscenes.ingame;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.ui.FontType;
import com.github.codestorm.bounceverse.systems.manager.metrics.LeaderboardManager;
import com.github.codestorm.bounceverse.typing.records.EndlessScore;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * <h1>{@link DeathSubscene}</h1>
 *
 * {@link SubScene} hiển thị khi người chơi thua (Game Over). <br>
 * Hiển thị điểm số và cho phép lưu vào leaderboard nếu đạt top.
 */
public class DeathSubscene extends SubScene {
    private Rectangle overlay;
    private VBox mainContainer;
    private final int score;
    private final int level;

    public final List<Runnable> onGotoMainMenuListeners = new ArrayList<>();

    public DeathSubscene(int score, int level) {
        this.score = score;
        this.level = level;
        final var canSavedToLeaderboard = LeaderboardManager.getInstance().isTopScore(score);

        final var root = new StackPane();

        // Lớp phủ màu đỏ trong suốt
        final var overlay = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        overlay.setFill(Color.rgb(255, 0, 0, 0.3));

        // Container chính
        final var mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setMaxWidth(500);

        // Tiêu đề "GAME OVER"
        final var gameOverText =
                FXGL.getUIFactoryService().newText("GAME OVER", Color.RED, FontType.MONO, 48.0);
        final var scoreText =
                FXGL.getUIFactoryService()
                        .newText("Score: " + score, Color.WHITE, FontType.MONO, 24.0);
        final var levelText =
                FXGL.getUIFactoryService()
                        .newText("Level: " + level, Color.WHITE, FontType.MONO, 24.0);

        mainContainer.getChildren().addAll(gameOverText, scoreText, levelText);

        if (canSavedToLeaderboard) {
            final var congratsText =
                    FXGL.getUIFactoryService()
                            .newText(
                                    "Congratulations! You made it to the Top "
                                            + LeaderboardManager.MAX_SIZE
                                            + "!",
                                    Color.YELLOW,
                                    FontType.MONO,
                                    18.0);
            congratsText.setWrappingWidth(450);

            final var enterNameText =
                    FXGL.getUIFactoryService()
                            .newText("Enter your name:", Color.WHITE, FontType.UI, 16.0);
            final var nameField = new TextField();
            nameField.setPromptText("Enter name (alphanumeric only)");
            nameField.setMaxWidth(300);
            nameField.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.9);"
                            + "-fx-text-fill: black;"
                            + "-fx-font-size: 16px;"
                            + "-fx-padding: 10px;");
            nameField
                    .textProperty()
                    .addListener(
                            (obs, oldVal, newVal) -> {
                                if (!newVal.matches("[a-zA-Z0-9]*")) {
                                    nameField.setText(oldVal);
                                }
                                // Giới hạn độ dài tên
                                if (newVal.length() > 20) {
                                    nameField.setText(oldVal);
                                }
                            });
            final var saveButton = new Button("Save Score");
            saveButton.setStyle(
                    "-fx-background-color: #4CAF50;"
                            + "-fx-text-fill: white;"
                            + "-fx-font-size: 16px;"
                            + "-fx-padding: 10px 30px;"
                            + "-fx-cursor: hand;");
            saveButton.setOnAction(
                    e -> {
                        var name = nameField.getText().trim();
                        if (name.isEmpty()) {
                            name = "Anonymous";
                        }
                        saveToLeaderboard(name);
                        returnToMainMenu();
                    });

            saveButton.disableProperty().bind(nameField.textProperty().isEmpty());
            mainContainer.getChildren().addAll(congratsText, enterNameText, nameField, saveButton);
        }
        final var mainMenuButton = new Button("Return to Main Menu");
        mainMenuButton.setStyle(
                "-fx-background-color: #f44336;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 16px;"
                        + "-fx-padding: 10px 30px;"
                        + "-fx-cursor: hand;");
        mainMenuButton.setOnAction(e -> returnToMainMenu());
        mainContainer.getChildren().add(mainMenuButton);

        root.getChildren().addAll(overlay, mainContainer);
        root.setAlignment(Pos.CENTER);
        getContentRoot().getChildren().add(root);
    }

    /**
     * Lưu điểm số vào leaderboard.
     *
     * @param playerName Tên người chơi
     */
    private void saveToLeaderboard(String playerName) {
        final var newScore = new EndlessScore(playerName, score, level, Instant.now());
        LeaderboardManager.getInstance().addScore(newScore);
        FXGL.getNotificationService().pushNotification("Score saved to leaderboard!");
    }

    /** Trở về màn hình chính. */
    private void returnToMainMenu() {
        onGotoMainMenuListeners.forEach(Runnable::run);
        FXGL.getGameController().gotoMainMenu();
    }
}
