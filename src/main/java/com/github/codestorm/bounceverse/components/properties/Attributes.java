package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.Property;

/**
 *
 *
 * <h1>{@link Attributes}</h1>
 *
 * Các chỉ số thuộc tính chung (chưa cụ thể thành class) của {@link Entity} nói chung.
 */
public final class Attributes extends Property {
    public static final int DEFAULT_DEFENSE = 0;
    private int defense = DEFAULT_DEFENSE;

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
