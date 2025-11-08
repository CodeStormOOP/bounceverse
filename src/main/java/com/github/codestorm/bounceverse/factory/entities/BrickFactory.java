package com.github.codestorm.bounceverse.factory.entities;

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
import com.github.codestorm.bounceverse.components.behaviors.HealthDeath;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jetbrains.annotations.NotNull;

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
    private static final Color DEFAULT_COLOR = Color.LIGHTBLUE;
    private static final int DEFAULT_HP = 1;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        return null;
        // TODO: Hoàn thiện Builder
    }

    /**
     * Tạo mới một entity brick.
     *
     * @param pos Vị trí
     * @param hp HP
     * @param view Khung nhìn
     * @param components Các components thêm vào
     * @return Entity Brick mới tạo
     */
    @NotNull private static Entity newBrick(Point2D pos, int hp, Rectangle view, Component... components) {
        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();
        fixture.setFriction(0f);
        fixture.setRestitution(1f);

        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder()
                .type(EntityType.BRICK)
                .at(pos)
                .viewWithBBox(view)
                .collidable()
                .with(physics, new HealthIntComponent(hp), new HealthDeath())
                .with(components)
                .build();
    }

    /**
     * Tạo mới một entity brick với khung nhìn mặc định.
     *
     * @param pos Vị trí
     * @param hp HP
     * @param components Các components thêm vào
     * @return Entity Brick mới tạo
     */
    @NotNull private static Entity newBrick(Point2D pos, int hp, Component... components) {
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
