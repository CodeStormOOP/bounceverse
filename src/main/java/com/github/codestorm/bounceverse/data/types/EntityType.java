package com.github.codestorm.bounceverse.data.types;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;

/**
 *
 *
 * <h1>{@link EntityType}</h1>
 *
 * Loại của {@link Entity}, dùng để phân biệt giữa các entity có loại khác nhau. <br>
 * Sử dụng {@link EntityBuilder#type(Enum)} để gán cho entity và {@link Entity#getType()} để truy
 * xuất, hoặc {@link Entity#isType(Object)} để kiểm tra.
 *
 * @see EntityBuilder
 * @see Entity
 */
public enum EntityType {
    BRICK,
    PADDLE,
    BALL,
    POWER_UP,
    BULLET,
    WALL
}
