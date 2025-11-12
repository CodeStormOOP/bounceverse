package com.github.codestorm.bounceverse.components.properties.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.CoreComponent;
import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.AssetsPath;
import com.github.codestorm.bounceverse.components.Property;
import com.github.codestorm.bounceverse.typing.annotations.OnlyForEntity;
import com.github.codestorm.bounceverse.typing.enums.BrickType;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 *
 *
 * <h1>{@link BrickTextureManager}</h1>
 *
 * Sự thay đổi texture của {@link EntityType#BRICK} theo {@link HealthIntComponent};
 */
@CoreComponent
@Required(HealthIntComponent.class)
@OnlyForEntity({EntityType.BRICK})
public final class BrickTextureManager extends Property {
    private String oldTexturePath;
    private Node oldTexture;
    public final BrickType brickType;
    public final Color color;

    public BrickTextureManager(BrickType brickType, Color color) {
        this.brickType = brickType;
        this.color = color;
    }

    private static Node makeView(String texturePath, int width, int height) {
        var view = FXGL.getAssetLoader().loadTexture(texturePath, width, height);
        view.setSmooth(false);
        view.setPreserveRatio(false);
        return view;
    }

    private String getColorName() {
        if (color.equals(Color.BLUE)) return "blue";
        if (color.equals(Color.GREEN)) return "green";
        if (color.equals(Color.ORANGE)) return "orange";
        if (color.equals(Color.PINK)) return "pink";
        if (color.equals(Color.RED)) return "red";
        if (color.equals(Color.YELLOW)) return "yellow";
        return "blue";
    }

    @Override
    public void onUpdate(double tpf) {
        final var health = entity.getComponent(HealthIntComponent.class);
        final var percent = health.getValuePercent() / 100;

        // SỬA ĐỔI Ở ĐÂY
        final var colorKey = getColorName(); // Chuyển Color object thành String key
        final var colorTextures =
                AssetsPath.Textures.Bricks.COLORS.get(colorKey); // Dùng key String để tra cứu

        // Đoạn mã còn lại giữ nguyên
        final var texturePath = colorTextures.getTexture(brickType, percent);

        final var views = entity.getViewComponent();
        if (oldTexturePath == null) {
            oldTexturePath = texturePath;
            oldTexture = makeView(texturePath, (int) entity.getWidth(), (int) entity.getHeight());
            views.addChild(oldTexture);
        } else {
            if (!oldTexturePath.equals(texturePath)) {
                views.removeChild(oldTexture);
                oldTexturePath = texturePath;
                oldTexture =
                        makeView(texturePath, (int) entity.getWidth(), (int) entity.getHeight());
                views.addChild(oldTexture);
            }
        }
    }

    public Color getColor() {
        return this.color;
    }
}
