package com.github.rbaul.spring.boot.activity_log;

import com.github.rbaul.spring.boot.activity_log.config.ActivityLogConfig;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigurationPackage
@Import(ActivityLogConfig.class)
public class ActivityLogAutoConfiguration {
}
