package com.github.codestorm.bounceverse.scenes;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PauseMenu extends FXGLDefaultMenu {
    public PauseMenu() {
        super(MenuType.GAME_MENU);

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setTranslateY(120);

        Button btnResume = new Button("Resume");
        btnResume.setPrefWidth(200);
        btnResume.setOnAction(e -> getController().resumeEngine());

        Button btnExit = new Button("Exit");
        btnExit.setPrefWidth(200);
        btnExit.setOnAction(e -> getController().exit());

        root.getChildren().addAll(btnResume, btnExit);

        getContentRoot().getChildren().add(root);
    }
}
