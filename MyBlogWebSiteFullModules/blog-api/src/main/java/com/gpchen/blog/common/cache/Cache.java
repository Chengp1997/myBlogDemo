package com.gpchen.blog.common.cache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {

    //expire time for cache
    long expire() default 1*60*1000;
    //default name,as the key we are going to store in redis
    String name() default "";
}
