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
public class InnerExampleObject {
    private String innerField1 = "inner_field1_String";

    private Integer innerField2 = 2;

}
