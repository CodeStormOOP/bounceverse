package com.github.codestorm.bounceverse.components;

import com.github.codestorm.bounceverse.Bounceverse;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

/**
 *
 *
 * <h1>{@link Component}</h1>
 *
 * Một thành phần lưu trữ trên Entity trong game {@link Bounceverse}.<br>
 * Giống như phiên bản class của {@link com.almasb.fxgl.entity.component.Component}.
 *
 * @see com.almasb.fxgl.entity.component.Component
 */
public abstract sealed class Component extends com.almasb.fxgl.entity.component.Component
        permits Behavior, Property {
    @Override
    public void onAdded() {
        Utilities.Compatibility.throwIfNotCompatible((EntityType) entity.getType(), this);
    }
}
