package com.github.codestorm.bounceverse.components.properties.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;

public class PaddleTextureManager extends Component {

    private String color;
    private Texture paddleTexture;

    @Override
    public void onAdded() {
        this.color = FXGL.gets("paddleColor");
        this.paddleTexture = FXGL.texture(getTexturePath("normal"));
        
        // Kích thước ban đầu được thiết lập dựa trên hitbox
        paddleTexture.setFitWidth(entity.getWidth());
        paddleTexture.setFitHeight(entity.getHeight());

        entity.getViewComponent().addChild(paddleTexture);
    }

    public Texture getPaddleTexture() {
        return paddleTexture;
    }

    public void setNormalState() {
        updateTexture("normal");
    }

    public void setExpandState() {
        updateTexture("expand");
    }

    public void setShrinkState() {
        updateTexture("shrink");
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

    private void updateTexture(String state) {
        String path = getTexturePath(state);
        
        // Chỉ thay đổi hình ảnh bên trong Texture
        paddleTexture.setImage(FXGL.getAssetLoader().loadImage(path));

        paddleTexture.setFitHeight(entity.getHeight());
    }
}