package com.github.codestorm.bounceverse.components.properties.paddle;

import com.almasb.fxgl.entity.component.Component;

import javafx.scene.Node;

/**
 * Quản lý kích thước gốc của Paddle và cho phép reset.
 */
public class PaddleSizeManager extends Component {

    private double originalWidth;
    private double originalScaleX;

    @Override
    public void onAdded() {
        Node view = getEntity().getViewComponent().getChildren().get(0);
        originalWidth = view.getBoundsInLocal().getWidth();
        originalScaleX = view.getScaleX();
    }

    /** Phóng to paddle theo hệ số. */
    public void expand(double factor) {
        Node view = getEntity().getViewComponent().getChildren().get(0);
        view.setScaleX(factor);
    }

    /** Thu nhỏ paddle theo hệ số. */
    public void shrink(double factor) {
        Node view = getEntity().getViewComponent().getChildren().get(0);
        view.setScaleX(factor);
    }

    /** Reset paddle về kích thước gốc. */
    public void resetSize() {
        Node view = getEntity().getViewComponent().getChildren().get(0);
        view.setScaleX(originalScaleX);
    }
}
