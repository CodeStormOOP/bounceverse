package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.typing.annotations.ForEntity;
import java.util.Arrays;
import java.util.EnumSet;
import javafx.geometry.Side;

/**
 *
 *
 * <h1>{@link Shield}</h1>
 *
 * Khiên bảo vệ {@link Entity}. Khiên có thể bảo vệ một hoặc nhiều phía khỏi bị tấn công.
 */
@ForEntity({})
public class Shield extends Property {
    private EnumSet<Side> sides = EnumSet.noneOf(Side.class);

    /**
     * Kiểm tra khiên có bảo vệ được cạnh mong muốn không.
     *
     * @param sides Cạnh cần kiểm tra
     * @return {@code true} nếu bảo vệ được, ngược lại {@code false}
     */
    public boolean hasSide(Side... sides) {
        return this.sides.containsAll(Arrays.asList(sides));
    }

    public EnumSet<Side> getSides() {
        return sides;
    }

    public void setSides(EnumSet<Side> sides) {
        this.sides = sides;
    }

    public void addSide(Side... sides) {
        this.sides.addAll(Arrays.asList(sides));
    }

    public void removeSide(Side... sides) {
        Arrays.asList(sides).forEach(this.sides::remove);
    }

    public Shield() {}

    public Shield(Side... sides) {
        addSide(sides);
    }
}
