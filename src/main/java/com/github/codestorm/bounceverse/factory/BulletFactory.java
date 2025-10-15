package com.github.codestorm.bounceverse.factory;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.github.codestorm.bounceverse.components.properties.bullet.BulletVelocity;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public final class BulletFactory implements EntityFactory {
    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        return entityBuilder(data)
                .type(EntityType.BULLET)
                .viewWithBBox(new Circle(4, Color.YELLOW))
                .with(new CollidableComponent(true))
                .with(new BulletVelocity(data.get("speed"), data.get("direction")))
                .build();
    }
}
