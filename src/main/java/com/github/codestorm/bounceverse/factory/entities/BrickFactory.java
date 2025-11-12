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
import com.github.codestorm.bounceverse.components.properties.brick.BrickTextureManager;
import com.github.codestorm.bounceverse.typing.enums.BrickType;
import com.github.codestorm.bounceverse.typing.enums.CollisionGroup;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

public final class BrickFactory extends EntityFactory {

    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 30;
    private static final int DEFAULT_HP = 1;
    private static final Random RANDOM = new Random();
    private static final List<String> COLORS =
            List.of("blue", "green", "orange", "pink", "red", "yellow");

    private String getColorName(Color color) {
        if (color.equals(Color.BLUE)) {
            return "blue";
        }
        if (color.equals(Color.GREEN)) {
            return "green";
        }
        if (color.equals(Color.ORANGE)) {
            return "orange";
        }
        if (color.equals(Color.PINK)) {
            return "pink";
        }
        if (color.equals(Color.RED)) {
            return "red";
        }
        if (color.equals(Color.YELLOW)) {
            return "yellow";
        }
        return "blue";
    }

    // Phương thức trợ giúp để lấy Color object từ SpawnData
    private Color getSpawnColor(SpawnData data) {
        var colorValue = data.get("color");
        if (colorValue instanceof Color) {
            return (Color) colorValue;
        } else if (colorValue instanceof String) {
            return switch ((String) colorValue) {
                case "green" -> Color.GREEN;
                case "orange" -> Color.ORANGE;
                case "pink" -> Color.PINK;
                case "red" -> Color.RED;
                case "yellow" -> Color.YELLOW;
                default -> Color.BLUE;
            };
        }
        return Color.BLUE; // Mặc định
    }

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        var hp = ((Number) Utilities.Typing.getOr(data, "hp", DEFAULT_HP)).intValue();
        var width = ((Number) Utilities.Typing.getOr(data, "width", DEFAULT_WIDTH)).intValue();
        var height = ((Number) Utilities.Typing.getOr(data, "height", DEFAULT_HEIGHT)).intValue();

        Point2D pos = data.hasKey("pos") ? data.get("pos") : new Point2D(data.getX(), data.getY());

        // CHỈ LẤY STRING KEY: Đảm bảo chỉ làm việc với String ở đây
        String colorKey;
        var colorValue = data.get("color");

        if (colorValue instanceof Color) {
            colorKey = getColorName((Color) colorValue); // Chuyển Color object thành String key
        } else if (colorValue instanceof String) {
            colorKey = (String) colorValue;
        } else {
            colorKey = COLORS.get(RANDOM.nextInt(COLORS.size()));
        }

        var colorAsset = AssetsPath.Textures.Bricks.COLORS.get(colorKey);
        if (colorAsset == null) {
            colorAsset =
                    AssetsPath.Textures.Bricks.COLORS.values().stream()
                            .findFirst()
                            .orElseThrow(
                                    () ->
                                            new IllegalStateException(
                                                    "No ColorAssets available for bricks"));
        }

        var type = Utilities.Typing.getOr(data, "type", BrickType.NORMAL);
        var texturePath = colorAsset.getTexture(type, 1.0);
        var texture = FXGL.texture(texturePath);
        texture.setFitWidth(width);
        texture.setFitHeight(height);

        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);
        fixture.getFilter().categoryBits = CollisionGroup.BRICK.bits;
        fixture.getFilter().maskBits = CollisionGroup.BALL.bits | CollisionGroup.BULLET.bits;
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

    @Spawns("normalBrick")
    public Entity newNormalBrick(SpawnData data) {
        data.put("type", BrickType.NORMAL);
        data.put("hp", (double) DEFAULT_HP);
        var color = getSpawnColor(data);
        data.put("color", getColorName(color));
        return getBuilder(data)
                .with(new BrickTextureManager(BrickType.NORMAL, color))
                .buildAndAttach();
    }

    @Spawns("strongBrick")
    public Entity newStrongBrick(SpawnData data) {
        data.put("type", BrickType.STRONG);
        data.put("hp", DEFAULT_HP + 2);
        var color = getSpawnColor(data);
        data.put("color", getColorName(color));
        return getBuilder(data)
                .with(new StrongBrickTextureUpdater().withColor(color))
                .with(new BrickTextureManager(BrickType.STRONG, color))
                .buildAndAttach();
    }

    @Spawns("shieldBrick")
    public Entity newShieldBrick(SpawnData data) {
        data.put("type", BrickType.SHIELD);
        data.put("hp", (double) (DEFAULT_HP));
        var shield = new Shield(Side.LEFT, Side.RIGHT, Side.BOTTOM);
        var color = getSpawnColor(data);
        data.put("color", getColorName(color));
        return getBuilder(data)
                .with(shield)
                .with(new BrickTextureManager(BrickType.SHIELD, color))
                .buildAndAttach();
    }

    @Spawns("explodingBrick")
    public Entity newExplodingBrick(SpawnData data) {
        data.put("type", BrickType.EXPLODING);
        data.put("hp", (double) DEFAULT_HP);
        var explosionWidth = DEFAULT_WIDTH * 1.5;
        var explosionHeight = DEFAULT_HEIGHT * 2.5;
        var explosion = new Explosion(explosionWidth, explosionHeight);
        var color = getSpawnColor(data);
        data.put("color", getColorName(color));
        return getBuilder(data)
                .with(explosion)
                .with(new BrickTextureManager(BrickType.EXPLODING, color))
                .buildAndAttach();
    }

    @Spawns("keyBrick")
    public Entity newKeyBrick(SpawnData data) {
        data.put("type", BrickType.KEY);
        data.put("hp", (double) DEFAULT_HP);
        var special = new Special();
        var color = getSpawnColor(data);
        data.put("color", getColorName(color));
        return getBuilder(data)
                .with(special)
                .with(new BrickTextureManager(BrickType.KEY, color))
                .buildAndAttach();
    }
}
