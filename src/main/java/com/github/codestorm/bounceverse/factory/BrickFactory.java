package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.brick.Brick;
import com.github.codestorm.bounceverse.components.brick.properties.BrickHealth;
import com.github.codestorm.bounceverse.tags.ForBrick;
import com.github.codestorm.bounceverse.tags.Optional;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * <h1><b>BrickFactory</b></h1>
 *
 * <p>Factory để tạo các entity Brick trong trò chơi.
 *
 * @see EntityFactory
 */
public class BrickFactory implements EntityFactory {
    private static final int DEFAULT_X = 80;
    private static final int DEFAULT_Y = 30;
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
    @Spawns("brick")
    public static <OptionalBrickComponent extends Component & ForBrick & Optional> Entity newBrick(
            Point2D pos, int hp, Rectangle view, OptionalBrickComponent... components) {
        return FXGL.entityBuilder()
                .at(pos)
                .viewWithBBox(view)
                .with(new Brick(new BrickHealth(hp)))
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
    @Spawns("brick")
    public static <OptionalBrickComponent extends Component & ForBrick & Optional> Entity newBrick(
            Point2D pos, int hp, OptionalBrickComponent... components) {
        return newBrick(pos, hp, new Rectangle(DEFAULT_X, DEFAULT_Y, DEFAULT_COLOR), components);
    }

    /**
     * Tạo mới một entity brick với khung nhìn và HP mặc định.
     *
     * @param pos Vị trí
     * @param components Các components tùy chọn
     * @return Entity Brick mới tạo
     * @param <OptionalBrickComponent> Component không bắt buộc phải có của Brick
     */
    @NotNull @SafeVarargs
    @Spawns("brick")
    public static <OptionalBrickComponent extends Component & ForBrick & Optional> Entity newBrick(
            Point2D pos, OptionalBrickComponent... components) {
        return newBrick(pos, DEFAULT_HP, components);
    }
}
