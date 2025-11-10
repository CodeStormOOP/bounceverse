package com.github.codestorm.bounceverse.typing.annotations;

import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.Component;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * <h1>@{@link OnlyForEntity}</h1>
 *
 * Ràng buộc đánh dấu class chỉ định là chỉ phù hợp cho entity nào. <br>
 * <br>
 * Sử dụng {@link Utilities.Compatibility#throwIfNotCompatible(EntityType, Component...)} để kiểm
 * tra. <br>
 * <br>
 *
 * <h2>Ví dụ</h2>
 *
 * <ul>
 *   <li>Cho phép tất cả: Không thêm annotation này.
 *   <li>Cho phép cụ thể: {@code @OnlyForEntity(A)} hoặc {@code @OnlyForEntity({A, B, C})}.
 *   <li>Không cho phép: {@code @OnlyForEntity({})}.
 * </ul>
 *
 * @see EntityType
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OnlyForEntity {
    EntityType[] value();
}
