package com.github.codestorm.bounceverse.typing.annotations;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * <h1>@{@link ForEntity}</h1>
 *
 * Đánh dấu class chỉ định là phù hợp cho entity nào. <br>
 * <b>Nếu chỉ định tất cả entity, hãy truyền vào mảng rỗng {@code {}}.</b> <br>
 * <br>
 * Sử dụng {@link Utilities.Compatibility#throwIfNotCompatible(EntityType, Component...)} để kiểm
 * tra.
 *
 * @see EntityType
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ForEntity {
    EntityType[] value();
}
