package com.github.codestorm.bounceverse.components.base.properties;

import com.github.codestorm.bounceverse.components.base.PropertyComponent;
import com.github.codestorm.bounceverse.data.types.Side;
import com.github.codestorm.bounceverse.tags.Optional;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 *
 * <h1><b>Shield</b></h1>
 *
 * <p>Lớp này đại diện cho Khiên bảo vệ Entity. Khiên có thể bảo vệ Entity từ một hoặc nhiều phía
 * khỏi bị tấn công.
 */
public abstract class Shield extends PropertyComponent implements Optional {
    private HashSet<Side> sides = new HashSet<>();

    public HashSet<Side> getSides() {
        return sides;
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
