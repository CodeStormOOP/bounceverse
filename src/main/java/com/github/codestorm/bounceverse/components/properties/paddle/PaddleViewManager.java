package com.github.codestorm.bounceverse.components.properties.paddle;

import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;

/**
 * Component duy nhất quản lý TOÀN BỘ hình ảnh và kích thước của Paddle.
 * Nó thay thế cho cả PaddleSizeManager và PaddleTextureManager.
 */
public class PaddleViewManager extends Component {

    private String color;
    private Texture paddleTexture;
    private BoundingBoxComponent bbox;

    private double originalWidth;
    private double originalHeight;

    private String currentState = "normal";
    private double currentSizeFactor = 1.0;

    private final List<Entity> clones = new ArrayList<>();

    // Các hệ số kích thước cho từng trạng thái
    private static final double EXPAND_FACTOR = 1.5;
    private static final double SHRINK_FACTOR = 0.7;

    @Override
    public void onAdded() {
        this.color = FXGL.gets("paddleColor");
        this.bbox = entity.getBoundingBoxComponent();

        this.originalWidth = entity.getWidth();
        this.originalHeight = entity.getHeight();

        // Khởi tạo Texture và thêm vào entity
        this.paddleTexture = FXGL.texture(getTexturePath("normal"));
        entity.getViewComponent().addChild(paddleTexture);

        // Cập nhật trạng thái ban đầu
        updateViewState("normal", 1.0);
    }

    // Các hàm công khai để các PowerUp gọi
    public void setNormalState() {
        updateViewState("normal", 1.0);
    }

    public void setExpandState() {
        updateViewState("expand", EXPAND_FACTOR);
    }

    public void setShrinkState() {
        updateViewState("shrink", SHRINK_FACTOR);
    }

    public void setPowerState() {
        updateViewState("power", 1.0);
    }

    public void reset() {
        setNormalState();
    }

    public void registerClone(Entity clone) {
        clones.add(clone);
        // Ngay khi đăng ký, đồng bộ hóa trạng thái hiện tại cho bản sao
        updateViewState(currentState, currentSizeFactor);
    }

    public void clearClones() {
        clones.clear();
    }

    /**
     * Hàm trung tâm, chịu trách nhiệm cập nhật MỌI THỨ.
     * 
     * @param state      Tên trạng thái ("normal", "expand", "shrink")
     * @param sizeFactor Hệ số nhân kích thước (1.0 cho normal)
     */
    private void updateViewState(String state, double sizeFactor) {
        this.currentState = state;
        this.currentSizeFactor = sizeFactor;

        double newWidth = originalWidth * sizeFactor;

        bbox.clearHitBoxes();
        bbox.addHitBox(new HitBox(BoundingShape.box(newWidth, originalHeight)));
        paddleTexture.setImage(FXGL.getAssetLoader().loadImage(getTexturePath(state)));
        paddleTexture.setFitWidth(newWidth);
        paddleTexture.setFitHeight(originalHeight);

        for (Entity clone : clones) {
            if (clone.isActive()) {
                // Cập nhật hitbox cho bản sao
                clone.getBoundingBoxComponent().clearHitBoxes();
                clone.getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(newWidth, originalHeight)));

                // Cập nhật hình ảnh cho bản sao
                var cloneTexture = (Texture) clone.getViewComponent().getChildren().get(0);
                cloneTexture.setImage(FXGL.getAssetLoader().loadImage(getTexturePath(state)));
                cloneTexture.setFitWidth(newWidth);
                cloneTexture.setFitHeight(originalHeight);
            }
        }

        paddleTexture.setImage(FXGL.getAssetLoader().loadImage(getTexturePath(state)));

        paddleTexture.setFitWidth(newWidth);
        paddleTexture.setFitHeight(originalHeight);
    }

    private String getTexturePath(String state) {
        String colorCapitalized = color.substring(0, 1).toUpperCase() + color.substring(1);
        String textureName;
        if ("normal".equals(state)) {
            textureName = colorCapitalized + " Paddle.png";
        } else {
            String stateCapitalized = state.substring(0, 1).toUpperCase() + state.substring(1);
            textureName = colorCapitalized + " " + stateCapitalized + " Paddle.png";
        }
        return "paddle/" + state + "/" + color + "/" + textureName;
    }
}