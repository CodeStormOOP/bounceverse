package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.components.properties.brick.BrickHealth;
import com.github.codestorm.bounceverse.data.tags.entities.ForBrick;
import com.github.codestorm.bounceverse.data.tags.requirements.OptionalTag;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * <h1>{@link BrickFactory}</h1>
 *
 * <p>Factory để tạo các entity loại {@link
 * com.github.codestorm.bounceverse.data.types.EntityType#BRICK} trong trò chơi.
 *
 * @see EntityFactory
 */
public final class BrickFactory implements EntityFactory {
    public static final int DEFAULT_WIDTH = 80;
    public static final int DEFAULT_HEIGHT = 30;
    private static final Color DEFAULT_COLOR = Color.LIGHTBLUE;
    private static final int DEFAULT_HP = 1;

    /**
     * Tạo mới một entity brick.
     *
     * @param pos Vị trí
     * @param hp HP
     * @param view Khung nhìn
     * @param components Các components tùy chọn
     * @return Entity Brick mới tạo
     * @param <OptionalBrickComponent> Component không bắt buộc phải có của Brick
     */
    @NotNull @SafeVarargs
    private static <OptionalBrickComponent extends Component & ForBrick & OptionalTag>
            Entity newBrick(
                    Point2D pos, int hp, Rectangle view, OptionalBrickComponent... components) {
        PhysicsComponent physics = new PhysicsComponent();

        FixtureDef fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);

        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder()
                .type(EntityType.BRICK)
                .at(pos)
                .viewWithBBox(view)
                .collidable()
                .with(physics)
                .with(new BrickHealth(hp))
                .with(components)
                .build();
    }

    /**
     * Tạo mới một entity brick với khung nhìn mặc định.
     *
     * @param pos Vị trí
     * @param hp HP
     * @param components Các components tùy chọn
     * @return Entity Brick mới tạo
     * @param <OptionalBrickComponent> Component không bắt buộc phải có của Brick
     */
    @NotNull @SafeVarargs
    private static <OptionalBrickComponent extends Component & ForBrick & OptionalTag>
            Entity newBrick(Point2D pos, int hp, OptionalBrickComponent... components) {
        return newBrick(
                pos, hp, new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLOR), components);
    }

    /**
     * Tạo entity Brick bình thường.
     *
     * @param pos Vị trí
     * @return Entity Brick mới tạo
     */
    @NotNull @Spawns("normalBrick")
    public static Entity newNormalBrick(SpawnData pos) {
        return newBrick(new Point2D(pos.getX(), pos.getY()), DEFAULT_HP);
    }
}
