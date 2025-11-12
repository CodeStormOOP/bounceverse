package com.github.codestorm.bounceverse.components.properties.paddle;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;

/**
 * Quản lý kích thước gốc của Paddle và cho phép reset.
 */
public class PaddleSizeManager extends Component {

    private BoundingBoxComponent bbox;
    private Texture view;
    private double originalWidth;

    @Override
    public void onAdded() {
        this.bbox = getEntity().getBoundingBoxComponent();
        this.view = getEntity().getComponent(PaddleTextureManager.class).getPaddleTexture();
        this.originalWidth = this.bbox.getWidth();
    }

    private void changeWidth(double newWidth) {
        if (view == null || bbox == null) {
            return;
        }
        view.setFitWidth(newWidth);
        bbox.clearHitBoxes();
        bbox.addHitBox(new HitBox(BoundingShape.box(newWidth, bbox.getHeight())));
    }

    /** Phóng to paddle theo hệ số. */
    public void expand(double factor) {
        changeWidth(originalWidth * factor);
    }

    /**
     * Thu nhỏ paddle theo hệ số.
     * Ví dụ: shrink(0.7) sẽ thu nhỏ paddle còn 70% kích thước gốc.
     */
    public void shrink(double factor) {

        changeWidth(originalWidth * factor);
    }

    public void resetSize() {
        changeWidth(originalWidth);
    }
}