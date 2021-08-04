package com.stan.zhangli.core.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，有该注解的缓存方法会抛出异常
 *
 * @author Stan
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheException {
}
