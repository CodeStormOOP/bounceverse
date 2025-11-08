package com.github.codestorm.bounceverse.factory.entities;

import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Explosion;
import com.github.codestorm.bounceverse.components.behaviors.HealthDeath;
import com.github.codestorm.bounceverse.components.behaviors.Special;
import com.github.codestorm.bounceverse.components.properties.Attributes;
import com.github.codestorm.bounceverse.components.properties.Shield;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    private static final List<String> NORMAL_TEXTURES = List.of(
            "bricks/normalBrick/06_test11.png",
            "bricks/normalBrick/07_test11.png",
            "bricks/normalBrick/08_test11.png",
            "bricks/normalBrick/09_test11.png",
            "bricks/normalBrick/10_test11.png",
            "bricks/normalBrick/11_test11.png");

    private static final List<String> STRONG_TEXTURES = List.of(
            "bricks/strongBrick/12_test11.png");

    private static final List<String> SHIELD_TEXTURES = List.of(
            "bricks/shieldBrick/00_test11.png");

    private static final List<String> EXPLODING_TEXTURES = List.of(
            "bricks/shieldBrick/00_test11.png");

    private static final List<String> SPECIAL_TEXTURES = List.of(
            "bricks/specialBrick/special_01.png");

    private static Node makeView(String texturePath) {
        ImageView view = new ImageView(FXGL.image(texturePath));
        view.setFitWidth(DEFAULT_WIDTH);
        view.setFitHeight(DEFAULT_HEIGHT);
        view.setSmooth(false);
        view.setPreserveRatio(false);
        return view;
    }

    private static Node makeColorView(Color color) {
        return new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, color);
    }

    /**
     * Tạo mới một entity brick.
     *
     * @param pos        Vị trí
     * @param hp         HP
     * @param view       Khung nhìn
     * @param components Các components thêm vào
     * @return Entity Brick mới tạo
     */
    @NotNull
    private static Entity newBrick(Point2D pos, int hp, Node view, Component... components) {
        Utilities.Compatibility.throwIfNotCompatible(EntityType.BRICK, components);

        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);
        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        var builder = FXGL.entityBuilder()
                .type(EntityType.BRICK)
                .at(pos)
                .viewWithBBox(view)
                .collidable()
                .with(physics, new Attributes(), new HealthIntComponent(hp), new HealthDeath());

        if (components != null && components.length > 0)
            builder.with(components);

        return builder.build();
    }

    /**
     * Tạo entity Brick bình thường.
     *
     * @param pos Vị trí
     * @return Entity Brick mới tạo
     */
    @Spawns("normalBrick")
    public static Entity newNormalBrick(SpawnData data) {
        String tex = NORMAL_TEXTURES.get(RANDOM.nextInt(NORMAL_TEXTURES.size()));
        return newBrick(new Point2D(data.getX(), data.getY()), DEFAULT_HP, makeView(tex));
    }

    /**
     * Tạo entity Brick với nhiều hp hơn
     *
     * @param pos Vị trí
     * @return Entity Brick mới tạo
     */
    @Spawns("strongBrick")
    public static Entity newStrongBrick(SpawnData data) {
        String tex = STRONG_TEXTURES.get(RANDOM.nextInt(STRONG_TEXTURES.size()));
        return newBrick(new Point2D(data.getX(), data.getY()), DEFAULT_HP + 2, makeView(tex));
    }

    /**
     * Tạo entity Shield Brick chỉ được phá từ trên xuống
     *
     * @param pos Vị trí
     * @return Entity Brick
     */
    @NotNull
    @Spawns("shieldBrick")
    public static Entity newShieldBrick(SpawnData data) {
        String tex = SHIELD_TEXTURES.get(RANDOM.nextInt(SHIELD_TEXTURES.size()));
        var shield = new Shield();
        shield.addSide(Side.LEFT, Side.RIGHT, Side.BOTTOM);
        var brick = newBrick(new Point2D(data.getX(), data.getY()), DEFAULT_HP + 1, makeView(tex));
        brick.addComponent(shield);
        return brick;
    }

    /**
     * Tạo entity Exploding Brick
     *
     * Exploding Brick – nổ phá gạch lân cận
     */
    @Spawns("explodingBrick")
    public static Entity newExplodingBrick(SpawnData data) {
        String tex = EXPLODING_TEXTURES.get(RANDOM.nextInt(EXPLODING_TEXTURES.size()));
        var explosion = new Explosion(120);
        return newBrick(new Point2D(data.getX(), data.getY()), DEFAULT_HP, makeView(tex), explosion);
    }

    @Spawns("specialBrick")
    public static Entity newSpecialBrick(SpawnData data) {
        String tex = SPECIAL_TEXTURES.get(RANDOM.nextInt(SPECIAL_TEXTURES.size()));
        return newBrick(new Point2D(data.getX(), data.getY()), DEFAULT_HP, makeView(tex), new Special());
    }

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBuilder'");
    }

    
}
