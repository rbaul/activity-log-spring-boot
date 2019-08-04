package com.github.rbaul.spring.boot.activity_log.sample.config;

import com.github.rbaul.spring.boot.activity_log.config.EnableActivityLog;
import com.github.rbaul.spring.boot.activity_log.sample.receivers.ActivityLogReceiverErrorImpl;
import com.github.rbaul.spring.boot.activity_log.sample.receivers.ActivityLogReceiverInfoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableActivityLog
public class ActivityLogConfig {

    @Bean
    public ActivityLogReceiverInfoImpl activityLogReceiverInfo() {
        return new ActivityLogReceiverInfoImpl();
    }

    @Bean
    public ActivityLogReceiverErrorImpl activityLogReceiverError() {
        return new ActivityLogReceiverErrorImpl();
    }

}