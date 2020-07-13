package com.github.rbaul.spring.boot.activity_log.sample.services;

import com.github.rbaul.spring.boot.activity_log.config.ActivityLog;
import com.github.rbaul.spring.boot.activity_log.sample.services.objects.ExampleObject;
import com.github.rbaul.spring.boot.activity_log.sample.services.objects.InnerExampleObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExampleServiceImpl {

    @ActivityLog("Action on method1: with {{str1}} and {{str2}}")
    public void method1(String str1, String str2) {
        log.info("method1({},{})", str1, str2);
    }

    @ActivityLog("Action on method2: with " +
            "{{exampleObject." + ExampleObject.Fields.field1 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field2 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field3 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field4 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field5 + "}}")
    public void method2(ExampleObject exampleObject) {
        log.info("method2({})", exampleObject);
    }

    @ActivityLog("Action on method3: with " +
            "{{exampleObject." + ExampleObject.Fields.field1 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field2 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field3 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field4 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.field5 + "}} throw exception")
    public void method3(ExampleObject exampleObject) {
        throw new RuntimeException("method3: Oooopppppsss");
    }

    @ActivityLog(value = "Action on method4: with {{str1}}", onPre = true)
    public void method4(String str1) {
        log.info("method4({})", str1);
    }

    @ActivityLog("Action on method5: with " +
            "{{exampleObject." + ExampleObject.Fields.field1 + "}} and " +
            "Inner object: {{exampleObject." + ExampleObject.Fields.innerExampleObject + "." + InnerExampleObject.Fields.innerField1 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.innerExampleObject + "." + InnerExampleObject.Fields.innerField2 + "}}")
    public void method5(ExampleObject exampleObject) {
        log.info("method5({})", exampleObject);
    }


    @ActivityLog("Action on method6: with {{#responseMethod(str1)}}")
    public void method6(String str1) {
        log.info("method6({})", str1);
    }

    @ActivityLog("Action on method9: with {{#responseMethod()}}")
    public void method9(String str1) {
        log.info("method9({})", str1);
    }

    @ActivityLog("Action on method7: with {{@exampleServiceImpl.responseMethod()}}")
    public void method7(String str1) {
        log.info("method7({})", str1);
    }

    @ActivityLog("Action on method8: with {{@exampleServiceImpl.responseMethod(str1)}}")
    public void method8(String str1) {
        log.info("method8({})", str1);
    }

    @ActivityLog("Action on method10: with {{@exampleServiceImpl.responseMethod(exampleObject." + ExampleObject.Fields.field1 + ")}}")
    public void method10(ExampleObject exampleObject) {
        log.info("method10({})", exampleObject);
    }

    public String responseMethod() {
        return "Response empty";
    }

    public String responseMethod(String str1) {
        return "Response " + str1;
    }
}
