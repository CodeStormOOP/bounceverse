package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.SpawnData;

/**
 *
 *
 * <h1>{@link EntityFactory}</h1>
 *
 * Một Factory chỉ chuyên sản xuất một loại Entity nào đó.
 *
 * @see com.almasb.fxgl.entity.EntityFactory
 */
abstract class EntityFactory implements com.almasb.fxgl.entity.EntityFactory {
    /**
     * Lấy {@link EntityBuilder} chung của entity tương ứng.
     *
     * @param data Dữ liệu (chung nhất) phục vụ spawn entity
     * @return Entity builder
     */
    protected abstract EntityBuilder getBuilder(SpawnData data);
}
