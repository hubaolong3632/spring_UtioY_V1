package com.example.UtioyV1.utio.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户角色权限注解
 * 用于标记需要权限校验的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})  // 可以标注在方法或类上
@Retention(RetentionPolicy.RUNTIME)  // 运行时可见，这样才能通过反射获取
public @interface UserRole {

        // 可选权限数组
        String[] value() default {};
        String[] and() default {};
}
