package com.github.codestorm.bounceverse.factory.entities;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType; // Thêm import này
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.typing.enums.CollisionGroup;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** Factory để tạo các entity loại BULLET trong trò chơi. */
public final class BulletFactory extends EntityFactory {
    public static final double DEFAULT_RADIUS = 4;
    public static final Color DEFAULT_COLOR = Color.YELLOW;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        var physics = new PhysicsComponent();
        var fixture = new FixtureDef();

        // Lọc va chạm: Đạn thuộc nhóm BULLET và chỉ va chạm với BRICK, WALL
        fixture.getFilter().categoryBits = CollisionGroup.BULLET.bits;
        fixture.getFilter().maskBits = CollisionGroup.BRICK.bits | CollisionGroup.WALL.bits;

        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.DYNAMIC);

        physics.setOnPhysicsInitialized(
                () -> {
                    Vec2 velocity = data.get("velocity");
                    physics.setLinearVelocity(velocity.toPoint2D());
                    physics.getBody().setGravityScale(0f);
                    physics.getBody().setBullet(true);
                });

        return entityBuilder(data).type(EntityType.BULLET).collidable().with(physics);
    }

    @Spawns("paddleBullet")
    public Entity newBullet(SpawnData data) {
        final var pos = new Point2D(data.getX(), data.getY());
        final double radius = Utilities.Typing.getOr(data, "radius", DEFAULT_RADIUS);
        final var color = Utilities.Typing.getOr(data, "color", DEFAULT_COLOR);
        final int damage = Utilities.Typing.getOr(data, "damage", Attack.DEFAULT_DAMAGE);
        final var attackComponent = new Attack(damage);

        final var bbox = new Circle(radius, color);

        return getBuilder(data).at(pos).viewWithBBox(bbox).with(attackComponent).buildAndAttach();
    }
}
