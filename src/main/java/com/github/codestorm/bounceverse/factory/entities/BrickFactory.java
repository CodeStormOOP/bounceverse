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
import com.github.codestorm.bounceverse.components.behaviors.HealthDeath;
import com.github.codestorm.bounceverse.components.properties.Attributes;
import com.github.codestorm.bounceverse.components.properties.Shield;
import com.github.codestorm.bounceverse.components.properties.brick.BrickTextureManager;
import com.github.codestorm.bounceverse.typing.enums.BrickType;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 *
 *
 * <h1>{@link BrickFactory}</h1>
 *
 * <br>
 * Factory để tạo các entity loại {@link EntityType#BRICK} trong trò chơi.
 *
 * @see EntityFactory
 */
public final class BrickFactory extends EntityFactory {
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 30;
    private static final int DEFAULT_HP = 1;
    private static final Random RANDOM = new Random();

    private static Color getRandomColor() {
        final var colors = AssetsPath.Textures.Bricks.COLORS.keySet().toArray(new Color[0]);
        return colors[RANDOM.nextInt(colors.length)];
    }

    private static Node makeView(String texturePath, int width, int height) {
        var view = FXGL.getAssetLoader().loadTexture(texturePath, width, height);
        view.setSmooth(false);
        view.setPreserveRatio(false);
        return view;
    }

    /**
     * Tạo builder chung cho brick từ SpawnData.
     *
     * @param data SpawnData chứa pos, width, height, hp, color, brickType, components
     * @return EntityBuilder cho brick
     */
    @NotNull @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        int hp = Utilities.Typing.getOr(data, "hp", DEFAULT_HP);
        int width = Utilities.Typing.getOr(data, "width", DEFAULT_WIDTH);
        int height = Utilities.Typing.getOr(data, "height", DEFAULT_HEIGHT);
        Point2D pos = data.get("pos");
        BrickType brickType = data.get("type");
        var color = Utilities.Typing.getOr(data, "color", getRandomColor());

        final var shape = BoundingShape.box(width, height);

        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);
        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder(data)
                .type(EntityType.BRICK)
                .bbox(shape)
                .at(pos)
                .collidable()
                .with(
                        physics,
                        new Attributes(),
                        new HealthIntComponent(hp),
                        new HealthDeath(),
                        new BrickTextureManager(brickType, color));
    }

    /**
     * Tạo entity Brick bình thường.
     *
     * @param data SpawnData
     * @return Entity Brick mới tạo
     */
    @Spawns("normalBrick")
    public Entity newNormalBrick(SpawnData data) {
        data.put("type", BrickType.NORMAL);
        return getBuilder(data).build();
    }

    @Spawns("strongBrick")
    public Entity newStrongBrick(SpawnData data) {
        data.put("type", BrickType.STRONG);
        if (!data.hasKey("hp")) {
            data.put("hp", DEFAULT_HP + 2);
        }
        return getBuilder(data).build();
    }

    @Spawns("shieldBrick")
    public Entity newShieldBrick(SpawnData data) {
        if (!data.hasKey("hp")) {
            data.put("hp", DEFAULT_HP + 1);
        }
        data.put("type", BrickType.SHIELD);
        final var shield = new Shield(Side.LEFT, Side.RIGHT, Side.BOTTOM);

        return getBuilder(data).with(shield).build();
    }

    //    @Spawns("explodingBrick")
    //    public Entity newExplodingBrick(SpawnData data) {
    //        final var explosion = new Explosion(120);
    //
    //        data.put("type", BrickType.EXPLODING);
    //        return getBuilder(data).with(explosion).build();
    //    }
    //
    //    @Spawns("specialBrick")
    //    public Entity newSpecialBrick(SpawnData data) {
    //        final var dropPowerUp = new BrickDropPowerUp();
    //
    //        data.put("type", BrickType.SPECIAL);
    //        return getBuilder(data).with(dropPowerUp).build();
    //    }
}
