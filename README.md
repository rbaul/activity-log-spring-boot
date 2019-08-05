# Activity Log Spring Boot (Starter)
[Spring Boot](https://spring.io/projects/spring-boot) Starter for `Acitivity Log`.
> Activity Log easy way
> Create you owen receiver for activity log

[![Build Status](https://travis-ci.com/rbaul/activity-log-spring-boot.svg?branch=master)](https://travis-ci.com/rbaul/activity-log-spring-boot)
[![Sonatype Nexus (Snapshots) badge](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.github.rbaul/activity-log-spring-boot.svg)](https://oss.sonatype.org/#nexus-search;quick~activity-log-spring-boot)
[![CodeFactor](https://www.codefactor.io/repository/github/rbaul/activity-log-spring-boot/badge)](https://www.codefactor.io/repository/github/rbaul/activity-log-spring-boot)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Build on
>* Java 8
>* Spring Boot 2.1.2

## Setup
###### Maven dependency
```xml
<repositories>
    <repository>
        <id>Sonatype</id>
        <name>Sonatype's repository</name>
        <url>https://oss.sonatype.org/content/groups/public/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.rbaul</groupId>
    <artifactId>activity-log-spring-boot</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- Starter dependency -->
<dependency>
    <groupId>com.github.rbaul</groupId>
    <artifactId>activity-log-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

```

###### Gradle dependency
```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/groups/public" }
}

compile 'com.github.rbaul:activity-log-spring-boot:1.0.0-SNAPSHOT'
// Starter dependency
compile 'com.github.rbaul:activity-log-spring-boot-starter:1.0.0-SNAPSHOT'

```

###### Configuration
```java
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
```

###### Example receiver for active log
```java
@Slf4j
public class ActivityLogReceiverInfoImpl implements ActivityLogReceiver {

    @Override
    public void receive(ActivityLogObject activityLogObject) {
        log.info(">>> {}", activityLogObject);
    }
}
```

##### Example of usage
```java
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
```

####### Run Sample
> Run ActivityLogSampleApplication

```log
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.2.RELEASE)

2019-08-05 08:20:43.997  INFO 16968 --- [           main] g.r.s.b.a.s.ActivityLogSampleApplication : Starting ActivityLogSampleApplication on RBAUL02 with PID 16968 (C:\github\activity-log-spring-boot\activity-log-spring-boot-sample\out\production\classes started by rbaul in C:\github\activity-log-spring-boot)
2019-08-05 08:20:44.002  INFO 16968 --- [           main] g.r.s.b.a.s.ActivityLogSampleApplication : No active profile set, falling back to default profiles: default
2019-08-05 08:20:45.311  INFO 16968 --- [           main] g.r.s.b.a.s.ActivityLogSampleApplication : Started ActivityLogSampleApplication in 1.77 seconds (JVM running for 2.295)
2019-08-05 08:20:45.328  INFO 16968 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method1(argument1,argument2)
2019-08-05 08:20:45.333  INFO 16968 --- [           main] .r.s.b.a.s.r.ActivityLogReceiverInfoImpl : >>> ActivityLogObject(username=null, action=Action on method1: with argument1 and argument2, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.336 ERROR 16968 --- [           main] r.s.b.a.s.r.ActivityLogReceiverErrorImpl : >>> ActivityLogObject(username=null, action=Action on method1: with argument1 and argument2, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.339  INFO 16968 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method2(ExampleObject(field1=field1_String, field2=2, field3=Mon Aug 05 08:20:45 IDT 2019, field4=false, field5=true, innerExampleObject=InnerExampleObject(innerField1=inner_field1_String, innerField2=2)))
2019-08-05 08:20:45.339  INFO 16968 --- [           main] .r.s.b.a.s.r.ActivityLogReceiverInfoImpl : >>> ActivityLogObject(username=null, action=Action on method2: with field1_String and 2 and Mon Aug 05 08:20:45 IDT 2019 and false and true, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.339 ERROR 16968 --- [           main] r.s.b.a.s.r.ActivityLogReceiverErrorImpl : >>> ActivityLogObject(username=null, action=Action on method2: with field1_String and 2 and Mon Aug 05 08:20:45 IDT 2019 and false and true, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.340  INFO 16968 --- [           main] .r.s.b.a.s.r.ActivityLogReceiverInfoImpl : >>> ActivityLogObject(username=null, action=Action on method3: with field1_String and 2 and Mon Aug 05 08:20:45 IDT 2019 and false and true throw exception, time=Mon Aug 05 08:20:45 IDT 2019, status=FAILED)
2019-08-05 08:20:45.340 ERROR 16968 --- [           main] r.s.b.a.s.r.ActivityLogReceiverErrorImpl : >>> ActivityLogObject(username=null, action=Action on method3: with field1_String and 2 and Mon Aug 05 08:20:45 IDT 2019 and false and true throw exception, time=Mon Aug 05 08:20:45 IDT 2019, status=FAILED)
2019-08-05 08:20:45.340  INFO 16968 --- [           main] c.g.r.s.b.a.sample.ActivityLogRunner     : Oooopppppsss
2019-08-05 08:20:45.341  INFO 16968 --- [           main] .r.s.b.a.s.r.ActivityLogReceiverInfoImpl : >>> ActivityLogObject(username=null, action=Trying...Action on method4: with argument1, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.349 ERROR 16968 --- [           main] r.s.b.a.s.r.ActivityLogReceiverErrorImpl : >>> ActivityLogObject(username=null, action=Trying...Action on method4: with argument1, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.349  INFO 16968 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method4(argument1)
2019-08-05 08:20:45.349  INFO 16968 --- [           main] .r.s.b.a.s.r.ActivityLogReceiverInfoImpl : >>> ActivityLogObject(username=null, action=Action on method4: with argument1, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.349 ERROR 16968 --- [           main] r.s.b.a.s.r.ActivityLogReceiverErrorImpl : >>> ActivityLogObject(username=null, action=Action on method4: with argument1, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.351  INFO 16968 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method5(ExampleObject(field1=field1_String, field2=2, field3=Mon Aug 05 08:20:45 IDT 2019, field4=false, field5=true, innerExampleObject=InnerExampleObject(innerField1=inner_field1_String, innerField2=2)))
2019-08-05 08:20:45.351  INFO 16968 --- [           main] .r.s.b.a.s.r.ActivityLogReceiverInfoImpl : >>> ActivityLogObject(username=null, action=Action on method2: with field1_String and Inner object: inner_field1_String and 2, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)
2019-08-05 08:20:45.351 ERROR 16968 --- [           main] r.s.b.a.s.r.ActivityLogReceiverErrorImpl : >>> ActivityLogObject(username=null, action=Action on method2: with field1_String and Inner object: inner_field1_String and 2, time=Mon Aug 05 08:20:45 IDT 2019, status=SUCCESS)

Process finished with exit code 0
```

## License

Licensed under the [Apache License, Version 2.0].  

[Apache License, Version 2.0]: LICENSE