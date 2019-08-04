package com.github.rbaul.spring.boot.activity_log.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ActivityLogConfig.class)
public @interface EnableActivityLog {
}