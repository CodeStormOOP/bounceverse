package com.github.codestorm.bounceverse.data.tags;

/**
 *
 *
 * <h1>{@link Tag}</h1>
 *
 * <p>Tag cho class, được dùng để đánh dấu class theo tính chất. Thường được sử dụng thông qua
 * generic.
 */
public sealed interface Tag permits ComponentTag, EntityTag, RequirementTag {}
