package com.github.codestorm.bounceverse.ui.ingame;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.AssetsPath;
import com.github.codestorm.bounceverse.typing.structures.HealthIntValue;
import com.github.codestorm.bounceverse.ui.ViewElement;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

/**
 *
 *
 * <h1>{@link Hearts}</h1>
 *
 * Hiển thị số lượng trái tim (lives) của người chơi trên UI.
 */
public class Hearts extends ViewElement {
    public static final double DEFAULT_SPACING = 5;
    public static final Rectangle DEFAULT_SIZE = new Rectangle(32, 32);

    private final Texture single;
    private final HBox container = new HBox(DEFAULT_SPACING);

    public Hearts() {
        single =
                FXGL.getAssetLoader()
                        .loadTexture(
                                AssetsPath.Textures.HEART,
                                DEFAULT_SIZE.getWidth(),
                                DEFAULT_SIZE.getHeight());

        update();
        final HealthIntValue livesProps = FXGL.getWorldProperties().getObject("lives");
        livesProps.onChangedListeners.add(this::update);
    }

    @Override
    public void update() {
        final HealthIntValue livesProps = FXGL.getWorldProperties().getObject("lives");
        final var lives = livesProps.getValue();

        if (lives < 0) {
            container.getChildren().clear();
            return;
        }
        while (container.getChildren().size() > lives) {
            container.getChildren().removeLast();
        }
        while (container.getChildren().size() < lives) {
            var heart = single.copy();
            container.getChildren().add(heart);
        }
    }

    @Override
    public Node getView() {
        return container;
    }
}
