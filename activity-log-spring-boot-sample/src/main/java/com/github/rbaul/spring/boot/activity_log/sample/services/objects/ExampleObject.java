package com.github.rbaul.spring.boot.activity_log.sample.services.objects;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldNameConstants
public class ExampleObject {
    private String field1 = "field1_String";

    private Integer field2 = 2;

    private Date field3 = new Date();

    private boolean field4 = false;

    private Boolean field5 = true;

    private InnerExampleObject innerExampleObject = new InnerExampleObject();
}
