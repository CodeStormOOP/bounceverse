package com.github.codestorm.bounceverse.components.properties.paddle;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.shape.Rectangle;

/**
 * Quản lý kích thước gốc của Paddle và cho phép reset.
 */
public class PaddleSizeManager extends Component {

    private double originalWidth;
    private Rectangle view;
    private BoundingBoxComponent bbox;

    @Override
    public void onAdded() {
        // Lưu lại các tham chiếu cần thiết khi component được thêm vào
        view = (Rectangle) getEntity().getViewComponent().getChildren().get(0);
        bbox = getEntity().getComponent(BoundingBoxComponent.class);
        originalWidth = view.getWidth();
    }

    /**
     * Thay đổi chiều rộng của paddle.
     * 
     * @param newWidth Chiều rộng mới
     */
    private void changeWidth(double newWidth) {
        // 1. Cập nhật chiều rộng của hình ảnh (view)
        view.setWidth(newWidth);

        // 2. Cập nhật lại Bounding Box vật lý để khớp với kích thước mới
        bbox.clearHitBoxes();
        bbox.addHitBox(new HitBox(BoundingShape.box(newWidth, view.getHeight())));
    }

    /** Phóng to paddle theo hệ số. */
    public void expand(double factor) {
        changeWidth(originalWidth * factor);
    }

    /** Thu nhỏ paddle theo hệ số. */
    public void shrink(double factor) {
        changeWidth(originalWidth * factor);
    }

    /** Reset paddle về kích thước gốc. */
    public void resetSize() {
        changeWidth(originalWidth);
    }
}