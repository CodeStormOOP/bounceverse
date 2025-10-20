package com.github.codestorm.bounceverse.components.properties.brick;

import com.github.codestorm.bounceverse.components.properties.Shield;
import com.github.codestorm.bounceverse.data.meta.entities.ForEntity;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.geometry.Side;

/**
 *
 *
 * <h1>{@link BrickShield}</h1>
 *
 * <p>Lớp này đại diện cho Khiên bảo vệ Viên gạch.
 */
@ForEntity(EntityType.BRICK)
public final class BrickShield extends Shield {
    private static final int BONUS_SCORE = 20;

    public BrickShield() {}

    //    TODO: Add shield view
    //    case "top" -> shield = new Rectangle(-0.5, -1.5, DEFAULT_WIDTH + 1, thickness);
    //            case "bottom" ->
    //    shield =
    //            new Rectangle(
    //                                    -0.5,
    //                                    DEFAULT_HEIGHT - thickness + 1,
    //                                    DEFAULT_WIDTH + 1,
    //                                    thickness);
    //            case "left" -> shield = new Rectangle(-1.5, -0.5, thickness, DEFAULT_HEIGHT + 1);
    //            case "right" ->
    //    shield =
    //            new Rectangle(
    //            DEFAULT_WIDTH - thickness + 1,
    //                                    -0.5,
    //            thickness,
    //            DEFAULT_HEIGHT + 1);

    public BrickShield(Side... sides) {
        super(sides);
    }
}
