package com.github.codestorm.bounceverse.data.meta.entities;

import com.github.codestorm.bounceverse.data.types.EntityType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * <h1>@{@link SuitableEntity}</h1>
 *
 * Đánh dấu sự yêu cầu tham số truyền vào phải phù hợp với entity được chỉ định.
 *
 * @see ForEntity
 * @see EntityType
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface SuitableEntity {
    EntityType[] value();
}
