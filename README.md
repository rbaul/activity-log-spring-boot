# Activity Log Spring Boot (Starter)
[Spring Boot](https://spring.io/projects/spring-boot) Starter for `Activity Log`.
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
        throw new RuntimeException("Oooopppppsss");
    }

    @ActivityLog(value = "Action on method4: with {{str1}}", onPre = true)
    public void method4(String str1) {
        log.info("method4({})", str1);
    }

    @ActivityLog("Action on method2: with " +
            "{{exampleObject." + ExampleObject.Fields.field1 + "}} and " +
            "Inner object: {{exampleObject." + ExampleObject.Fields.innerExampleObject + "." + InnerExampleObject.Fields.innerField1 + "}} and " +
            "{{exampleObject." + ExampleObject.Fields.innerExampleObject + "." + InnerExampleObject.Fields.innerField2 + "}}")
    public void method5(ExampleObject exampleObject) {
        log.info("method5({})", exampleObject);
    }
```

##### Run Sample
> Run ActivityLogSampleApplication

```log
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.2.RELEASE)

2019-08-04 21:13:19.804  INFO 23884 --- [           main] g.r.s.b.a.s.ActivityLogSampleApplication : Starting ActivityLogSampleApplication on RBAUL02 with PID 23884 (C:\github\activity-log-spring-boot\activity-log-spring-boot-sample\out\production\classes started by rbaul in C:\github\activity-log-spring-boot)
2019-08-04 21:13:19.808  INFO 23884 --- [           main] g.r.s.b.a.s.ActivityLogSampleApplication : No active profile set, falling back to default profiles: default
2019-08-04 21:13:20.895  INFO 23884 --- [           main] g.r.s.b.a.s.ActivityLogSampleApplication : Started ActivityLogSampleApplication in 1.446 seconds (JVM running for 1.934)
2019-08-04 21:13:20.896  INFO 23884 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method1(argument1,argument2)
2019-08-04 21:13:20.898  INFO 23884 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method2(ExampleObject(field1=field1_String, field2=2, field3=Sun Aug 04 21:13:20 IDT 2019, field4=false, field5=true, innerExampleObject=InnerExampleObject(innerField1=inner_field1_String, innerField2=2)))
2019-08-04 21:13:20.900  INFO 23884 --- [           main] c.g.r.s.b.a.sample.ActivityLogRunner     : Oooopppppsss
2019-08-04 21:13:20.900  INFO 23884 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method4(argument1)
2019-08-04 21:13:20.900  INFO 23884 --- [           main] c.g.r.s.b.a.s.s.ExampleServiceImpl       : method5(ExampleObject(field1=field1_String, field2=2, field3=Sun Aug 04 21:13:20 IDT 2019, field4=false, field5=true, innerExampleObject=InnerExampleObject(innerField1=inner_field1_String, innerField2=2)))

Process finished with exit code 0
```

## License

Licensed under the [Apache License, Version 2.0].  

[Apache License, Version 2.0]: LICENSE
