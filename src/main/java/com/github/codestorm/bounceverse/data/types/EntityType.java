package com.github.codestorm.bounceverse.data.types;

import com.almasb.fxgl.entity.Entity;

/**
 *
 *
 * <h1>{@link EntityType}</h1>
 *
 * Loại của {@link com.almasb.fxgl.entity.Entity}, dùng để phân biệt giữa các entity có loại khác
 * nhau.
 *
 * <p>Sử dụng {@link com.almasb.fxgl.dsl.EntityBuilder#type(Enum)} để gán cho entity và {@link
 * Entity#getType()} để truy xuất, hoặc {@link Entity#isType(Object)} để kiểm tra
 *
 * @see com.almasb.fxgl.dsl.EntityBuilder
 * @see Entity
 */
public enum EntityType {
    BRICK,
    PADDLE,
    BALL,
    POWER_UP
}
