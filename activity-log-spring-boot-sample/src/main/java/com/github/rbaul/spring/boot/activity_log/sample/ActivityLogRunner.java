package com.github.rbaul.spring.boot.activity_log.sample;

import com.github.rbaul.spring.boot.activity_log.sample.services.ExampleServiceImpl;
import com.github.rbaul.spring.boot.activity_log.sample.services.objects.ExampleObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ActivityLogRunner implements ApplicationRunner {

    private final ExampleServiceImpl exampleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        exampleService.method1("argument1", "argument2");
        exampleService.method2(new ExampleObject());
        try {
            exampleService.method3(new ExampleObject());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        exampleService.method4("argument1");
        exampleService.method5(new ExampleObject());
    }
}
