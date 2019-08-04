package com.github.rbaul.spring.boot.activity_log.sample.receivers;

import com.github.rbaul.spring.boot.activity_log.objects.ActivityLogObject;
import com.github.rbaul.spring.boot.activity_log.ActivityLogReceiver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivityLogReceiverInfoImpl implements ActivityLogReceiver {

    @Override
    public void receive(ActivityLogObject activityLogObject) {
        log.info(">>> {}", activityLogObject);
    }
}
