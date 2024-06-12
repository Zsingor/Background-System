package com.example.springtest.aop.logs;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {
    /**模块*/
    String module() default "";

    /**描述*/
    String description() default "";
}
