package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.FallingComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpContainer;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;

/** Factory tạo Power-Up entity (rơi thẳng xuống, không dùng physics). */
public final class PowerUpFactory extends EntityFactory {

    public static final double DEFAULT_RADIUS = 10;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        return FXGL.entityBuilder().type(EntityType.POWER_UP).collidable();
    }

    /**
     * Tạo mới một PowerUp entity.
     *
     * @param data dữ liệu spawn
     * @return entity PowerUp
     */
    @Spawns("powerUp")
    public Entity newPowerUp(SpawnData data) {
        final double radius = Utilities.Typing.getOr(data, "radius", DEFAULT_RADIUS);

        // 🔹 Lấy danh sách power-up chứa bên trong (1 hoặc nhiều)
        final var containsData = data.hasKey("contains") ? data.get("contains") : new PowerUp[0];
        final var contains =
                containsData instanceof PowerUp p ? new PowerUp[] {p} : (PowerUp[]) containsData;

        // 🔹 Lấy vị trí spawn (có thể từ pos hoặc x,y)
        final Point2D pos =
                data.hasKey("pos") ? data.get("pos") : new Point2D(data.getX(), data.getY());

        // 🔹 Lấy texture (hoặc mặc định)
        final Texture texture =
                data.hasKey("texture")
                        ? data.get("texture")
                        : FXGL.texture("power/paddle/Expand Paddle.png");

        // 🔹 Giới hạn kích thước hiển thị
        texture.setFitWidth(42);
        texture.setFitHeight(42);
        texture.setPreserveRatio(true);

        // 🔹 Căn tâm ảnh để khi xoay hoặc va chạm không bị lệch
        texture.setTranslateX(-texture.getFitWidth() / 2);
        texture.setTranslateY(-texture.getFitHeight() / 2);

        final var hitbox = new HitBox(BoundingShape.circle(radius));

        return getBuilder(data)
                .bbox(hitbox)
                .at(pos)
                .view(texture)
                .with(new FallingComponent(), new PowerUpContainer(contains))
                .buildAndAttach();
    }
}
