package com.github.codestorm.bounceverse.data.tags.requirements;

import com.github.codestorm.bounceverse.data.tags.RequirementTag;

/**
 *
 *
 * <h1>{@link RequiredTag}</h1>
 *
 * Nhãn cho các thành phần yêu cầu phải có từ khi khởi tạo {@link com.almasb.fxgl.entity.Entity} và
 * gắn liền với đối tượng đó.
 *
 * <p><b>Chú ý:</b>
 *
 * <p>> Hãy thêm annotation {@link com.almasb.fxgl.entity.component.CoreComponent} vào final
 * component (tức component được trực tiếp gắn với entity).
 */
public interface RequiredTag extends RequirementTag {}
