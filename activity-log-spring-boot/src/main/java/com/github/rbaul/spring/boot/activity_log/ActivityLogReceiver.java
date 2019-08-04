package com.github.rbaul.spring.boot.activity_log;

import com.github.rbaul.spring.boot.activity_log.objects.ActivityLogObject;

public interface ActivityLogReceiver {
    void receive(ActivityLogObject activityLogObject);
}
