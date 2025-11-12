package com.github.codestorm.bounceverse.components.behaviors.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.AssetsPath;
import com.github.codestorm.bounceverse.typing.enums.BrickType;
import javafx.scene.paint.Color;

/**
 * Tự động đổi texture của Strong Brick dựa theo phần trăm HP còn lại.
 */
public class StrongBrickTextureUpdater extends Component {

    private BrickType brickType = BrickType.STRONG;
    private Color color = Color.BLUE;
    private Texture currentTexture;

    @Override
    public void onAdded() {
        var health = getEntity().getComponent(HealthIntComponent.class);
        health.valueProperty().addListener((obs, oldVal, newVal) -> updateTexture());
        updateTexture();
    }

    private void updateTexture() {
        var health = getEntity().getComponent(HealthIntComponent.class);
        double hpPercent = Math.max(0.0, health.getValue() / (double) health.getMaxValue());

        // Lấy texture tương ứng với HP còn lại
        var colorAssets = AssetsPath.Textures.Bricks.COLORS.get(getColorName());
        String texPath = colorAssets.getTexture(brickType, hpPercent);

        Texture tex = FXGL.texture(texPath);

        // Giữ nguyên kích thước ban đầu của brick
        double width = getEntity().getWidth();
        double height = getEntity().getHeight();
        tex.setFitWidth(width);
        tex.setFitHeight(height);

        getEntity().getViewComponent().clearChildren();
        getEntity().getViewComponent().addChild(tex);
        currentTexture = tex;
    }

    private String getColorName() {
        if (color.equals(Color.BLUE))
            return "blue";
        if (color.equals(Color.GREEN))
            return "green";
        if (color.equals(Color.ORANGE))
            return "orange";
        if (color.equals(Color.PINK))
            return "pink";
        if (color.equals(Color.RED))
            return "red";
        if (color.equals(Color.YELLOW))
            return "yellow";
        return "blue";
    }

    public StrongBrickTextureUpdater withColor(Color color) {
        this.color = color;
        return this;
    }
}
