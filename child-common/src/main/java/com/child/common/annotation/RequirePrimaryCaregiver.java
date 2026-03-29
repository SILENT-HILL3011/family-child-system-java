package com.child.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePrimaryCaregiver {

    String message() default "只有主力照料者才能执行此操作";
}
