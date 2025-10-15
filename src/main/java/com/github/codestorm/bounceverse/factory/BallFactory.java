package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.github.codestorm.bounceverse.components._old.ball.BallComponent;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 *
 * <h1><b>Ball Factory</b></h1>
 *
 * <p>This class defines and spawns a new {@code Ball} entity in the game world.
 *
 * <p>By default, the spawned ball has:
 *
 * <ul>
 *   <li>Radius = 25
 *   <li>Position: (x = 50, y = 50)
 *   <li>Color: Red
 * </ul>
 *
 * @author minngoc1213
 */
public class BallFactory implements EntityFactory {
    public static final int RADIUS = 10;

    @Spawns("ball")
    public Entity spawnBall(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.BALL)
                .collidable()
                .at(400, 400)
                .viewWithBBox(new Circle(RADIUS, Color.RED))
                .with(new BallComponent())
                .buildAndAttach();
    }
}
