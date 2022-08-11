package com.gpchen.blog.common.log;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    //module name
    String module() default "";
    //what operation
    String operation() default  "";
}
