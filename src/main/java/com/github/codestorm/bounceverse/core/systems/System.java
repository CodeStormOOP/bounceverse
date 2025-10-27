package com.github.codestorm.bounceverse.core.systems;

import com.github.codestorm.bounceverse.Bounceverse;

/**
 *
 *
 * <h1>{@link System}</h1>
 *
 * Hệ thống của game, đảm nhiệm cho một thành phần nào đó của game. <br>
 * <br>
 * <b>Các lớp kế thừa nên thiết kế dựa trên (lazy-loaded) Singleton.</b> <br>
 * Tất cả logic của hệ thống được áp dụng thông qua {@link #apply()}.
 *
 * <h3>FAQ</h3>
 *
 * <ul>
 *   <li><b>Q:</b> Tại sao lại phải tạo Singleton? Bộ tạo static method trên class không được à cha
 *       nội?? <br>
 *       <b>A</b>: Tất cả là tại abstract/interface không cho ghi đè static đó!!! 😭😭😭
 * </ul>
 */
abstract class System {
    protected System() {}

    /**
     * Áp dụng logic của hệ thống vào game. <br>
     * Sử dụng trên {@link Bounceverse}
     */
    public abstract void apply();
}
