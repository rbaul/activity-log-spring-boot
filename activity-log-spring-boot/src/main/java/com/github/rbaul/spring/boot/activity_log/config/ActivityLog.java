package com.github.rbaul.spring.boot.activity_log.config;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityLog {
    /**
     * Activity Log message template action
     *
     * @return activity log template
     */
    @AliasFor("template")
    String value() default "";

    /**
     * Activity Log message template action
     *
     * @return activity log template
     */
    @AliasFor("value")
    String template() default "";

    /**
     * @return notify activity Log on pre-action
     */
    boolean onPre() default false;

    /**
     * @return prefix on template on pre-action
     */
    String onPrePrefix() default "Trying...";

}
