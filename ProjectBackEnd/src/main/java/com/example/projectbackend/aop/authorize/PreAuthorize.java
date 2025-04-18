package com.example.projectbackend.aop.authorize;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    String value() default "";
}
