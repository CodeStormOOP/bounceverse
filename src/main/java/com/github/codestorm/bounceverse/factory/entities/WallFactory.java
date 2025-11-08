package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.typing.enums.AnchorPoint;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <h1>{@link WallFactory}</h1>
 *
 * Factory sinh ra 4 tường bao quanh màn chơi (top, bottom, left, right).
 * Các tường là entity tĩnh (STATIC) có va chạm vật lý.
 */
public final class WallFactory extends EntityFactory {

        public static final double DEFAULT_THICKNESS = 5;
        public static final Color DEFAULT_COLOR = Color.GRAY;

        /**
         * Tạo Wall entity có vật lý (static, friction = 0, restitution = 1).
         */
        public static Entity createWall(Point2D pos, double width, double height, Color color, String side) {
                Rectangle rect = new Rectangle(width, height, color);

                var fixture = new FixtureDef();
                fixture.setFriction(0f);
                fixture.setRestitution(1f);

                var physics = new PhysicsComponent();
                physics.setFixtureDef(fixture);
                physics.setBodyType(BodyType.STATIC);

                return FXGL.entityBuilder()
                                .type(EntityType.WALL)
                                .at(pos)
                                .bbox(new com.almasb.fxgl.physics.HitBox(
                                                com.almasb.fxgl.physics.BoundingShape.box(width, height)))
                                .view(rect)
                                .collidable()
                                .with(physics)
                                .with("side", side)
                                .build();
        }

        @Spawns("wallTop")
        public Entity newWallTop(SpawnData data) {
                var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
                double thickness = data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
                Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

                return createWall(
                                AnchorPoint.TOP_LEFT.of(appShape),
                                appShape.getWidth(),
                                thickness,
                                color,
                                "TOP");
        }

        @Spawns("wallBottom")
        public Entity newWallBottom(SpawnData data) {
                var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
                double thickness = data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
                Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

                return createWall(
                                AnchorPoint.BOTTOM_LEFT.of(appShape).subtract(0, thickness),
                                appShape.getWidth(),
                                thickness,
                                color,
                                "BOTTOM");
        }

        @Spawns("wallLeft")
        public Entity newWallLeft(SpawnData data) {
                var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
                double thickness = data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
                Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

                return createWall(
                                AnchorPoint.TOP_LEFT.of(appShape),
                                thickness,
                                appShape.getHeight(),
                                color,
                                "LEFT");
        }

        @Spawns("wallRight")
        public Entity newWallRight(SpawnData data) {
                var appShape = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
                double thickness = data.hasKey("thickness") ? data.get("thickness") : DEFAULT_THICKNESS;
                Color color = data.hasKey("color") ? data.get("color") : DEFAULT_COLOR;

                return createWall(
                                AnchorPoint.TOP_RIGHT.of(appShape).subtract(thickness, 0),
                                thickness,
                                appShape.getHeight(),
                                color,
                                "RIGHT");
        }

        @Override
        protected EntityBuilder getBuilder(SpawnData data) {
                var physics = new PhysicsComponent();
                var fixture = new FixtureDef();
                fixture.setFriction(0f);
                fixture.setRestitution(1f);
                physics.setFixtureDef(fixture);
                physics.setBodyType(BodyType.STATIC);

                return FXGL.entityBuilder()
                                .type(EntityType.WALL)
                                .collidable()
                                .with(physics)
                                .anchorFromCenter();
        }
}
