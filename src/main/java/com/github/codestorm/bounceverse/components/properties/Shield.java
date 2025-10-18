package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.data.tags.components.Property;
import java.util.Arrays;
import java.util.HashSet;
import javafx.geometry.Side;

/**
 *
 *
 * <h1>{@link Shield}</h1>
 *
 * <p>Lớp này đại diện cho Khiên bảo vệ Entity. Khiên có thể bảo vệ Entity từ một hoặc nhiều phía
 * khỏi bị tấn công.
 */
public abstract class Shield extends Component implements Property {
    private HashSet<Side> sides = new HashSet<>();

    public HashSet<Side> getSides() {
        return sides;
    }

    /**
     * Kiểm tra khiên có bảo vệ được cạnh mong muốn không.
     *
     * @param side Cạnh cần kiểm tra
     * @return {@code true} nếu cạnh được bảo vệ, ngược lại {@code false}
     */
    public boolean hasSide(Side side) {
        return sides.contains(side);
    }

    public void setSides(HashSet<Side> sides) {
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
