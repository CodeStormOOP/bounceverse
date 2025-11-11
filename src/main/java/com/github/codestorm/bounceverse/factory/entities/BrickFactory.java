package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.AssetsPath;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Explosion;
import com.github.codestorm.bounceverse.components.behaviors.HealthDeath;
import com.github.codestorm.bounceverse.components.behaviors.Special;
import com.github.codestorm.bounceverse.components.behaviors.brick.StrongBrickTextureUpdater;
import com.github.codestorm.bounceverse.components.properties.Attributes;
import com.github.codestorm.bounceverse.components.properties.Shield;
import com.github.codestorm.bounceverse.typing.enums.BrickType;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

/**
 * Factory sinh ra các loại gạch (Brick) trong trò chơi.
 */
public final class BrickFactory extends EntityFactory {

    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 30;
    private static final int DEFAULT_HP = 1;

    private static final Random RANDOM = new Random();
    private static final List<String> COLORS = List.of("blue", "green", "orange", "pink", "red", "yellow");

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        int hp = ((Number) Utilities.Typing.getOr(data, "hp", DEFAULT_HP)).intValue();
        int width = ((Number) Utilities.Typing.getOr(data, "width", DEFAULT_WIDTH)).intValue();
        int height = ((Number) Utilities.Typing.getOr(data, "height", DEFAULT_HEIGHT)).intValue();

        Point2D pos = new Point2D(data.getX(), data.getY());

        // 🔹 Nếu không truyền color, chọn random từ 6 màu
        String colorKey = Utilities.Typing.getOr(data, "color", COLORS.get(RANDOM.nextInt(COLORS.size())));

        var colorAsset = AssetsPath.Textures.Bricks.COLORS.get(colorKey);
        if (colorAsset == null) {
            // fallback an toàn nếu không tồn tại
            colorAsset = AssetsPath.Textures.Bricks.COLORS.values().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No ColorAssets available for bricks"));
        }

        BrickType type = Utilities.Typing.getOr(data, "type", BrickType.NORMAL);

        double hpPercent = 1.0;
        String texturePath = colorAsset.getTexture(type, hpPercent);

        var texture = FXGL.texture(texturePath);
        texture.setFitWidth(width);
        texture.setFitHeight(height);

        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);
        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder(data)
                .type(EntityType.BRICK)
                .bbox(BoundingShape.box(width, height))
                .viewWithBBox(texture)
                .at(pos)
                .collidable()
                .with(physics, new Attributes(), new HealthIntComponent(hp), new HealthDeath());
    }

    /** Gạch thường */
    @Spawns("normalBrick")
    public Entity newNormalBrick(SpawnData data) {
        data.put("type", BrickType.NORMAL);
        data.put("hp", (double) DEFAULT_HP);
        return getBuilder(data).buildAndAttach();
    }

    /** Gạch trâu (HP cao hơn) */
    @Spawns("strongBrick")
    public Entity newStrongBrick(SpawnData data) {
        data.put("type", BrickType.STRONG);
        data.put("hp", DEFAULT_HP + 2);

        // Xác định màu
        String colorKey = Utilities.Typing.getOr(data, "color", COLORS.get(RANDOM.nextInt(COLORS.size())));
        var color = switch (colorKey) {
            case "green" -> Color.GREEN;
            case "orange" -> Color.ORANGE;
            case "pink" -> Color.PINK;
            case "red" -> Color.RED;
            case "yellow" -> Color.YELLOW;
            default -> Color.BLUE;
        };

        return getBuilder(data)
                .with(new StrongBrickTextureUpdater().withColor(color))
                .buildAndAttach();
    }

    /** Gạch có khiên bảo vệ 3 phía (chỉ phá từ trên xuống) */
    @Spawns("shieldBrick")
    public Entity newShieldBrick(SpawnData data) {
        data.put("type", BrickType.SHIELD);
        data.put("hp", (double) (DEFAULT_HP + 1));
        var shield = new Shield(Side.LEFT, Side.RIGHT, Side.BOTTOM);
        return getBuilder(data).with(shield).buildAndAttach();
    }

    /** Gạch nổ — khi bị phá sẽ kích hoạt Explosion gây sát thương lan */
    @Spawns("explodingBrick")
    public Entity newExplodingBrick(SpawnData data) {
        data.put("type", BrickType.EXPLODING);
        data.put("hp", (double) DEFAULT_HP);
        var explosion = new Explosion(120);
        return getBuilder(data).with(explosion).buildAndAttach();
    }

    /**
     * Gạch khóa (Key Brick) — loại đặc biệt rơi PowerUp khi bị phá
     */
    @Spawns("keyBrick")
    public Entity newKeyBrick(SpawnData data) {
        data.put("type", BrickType.KEY);
        data.put("hp", (double) DEFAULT_HP);

        // Gắn component Special để rơi power-up khi bị phá
        var special = new Special();

        return getBuilder(data)
                .with(special)
                .buildAndAttach();
    }

}
