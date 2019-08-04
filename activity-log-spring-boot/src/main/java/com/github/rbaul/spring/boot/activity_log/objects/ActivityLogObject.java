package com.github.rbaul.spring.boot.activity_log.objects;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ActivityLogObject {
    private String username;

    private String action;

    @Builder.Default
    private Date time = new Date();

    private ActivityLogStatus status;
}
