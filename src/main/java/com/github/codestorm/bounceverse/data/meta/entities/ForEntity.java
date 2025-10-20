package com.github.codestorm.bounceverse.data.meta.entities;

import com.github.codestorm.bounceverse.data.types.EntityType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * <h1>@{@link ForEntity}</h1>
 *
 * Đánh dấu class chỉ định là phù hợp cho entity nào.
 *
 * @see SuitableEntity
 * @see EntityType
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ForEntity {
    EntityType[] value();
}
