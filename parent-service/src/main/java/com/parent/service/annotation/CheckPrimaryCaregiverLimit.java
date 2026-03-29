package com.parent.service.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPrimaryCaregiverLimit {
    String message() default "一个家庭最多只能有 2 名主力照料者";
}
