package com.github.codestorm.bounceverse.data.tags.requirements;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.CoreComponent;
import com.github.codestorm.bounceverse.data.tags.RequirementTag;

/**
 *
 *
 * <h1>{@link Required}</h1>
 *
 * Nhãn cho các thành phần yêu cầu phải có từ khi khởi tạo {@link Entity} và gắn liền với đối tượng
 * đó.
 *
 * <p><b>Chú ý: Hãy thêm annotation @{@link CoreComponent} vào {@link Component} đầu cuối (tức
 * component được gắn trực tiếp với entity).</b>
 *
 * @see Optional
 */
public interface Required extends RequirementTag {}
