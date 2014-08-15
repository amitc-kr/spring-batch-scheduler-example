spring-batch-scheduler-example
==============================

This is the test of spring batch which read from db table and write to db table (mysql) with Scheduler

Used Following

* spring-batch-core 3.0.1.RELEASE
* spring-boot-starter-batch 1.1.5.RELEASE
* spring-boot-starter-data-jpa 1.1.5.RELEASE
* mysql
* gradle


To @EnableScheduling & @Schedule - it needs a method with void return type

@Schedule have a lot of options to schedule a job like fixedRate, cron, so forth (can be found here: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html)
 * @Scheduled(fixedDelay=5000)
 * @Scheduled(initialDelay=1000, fixedRate=5000)
 * @Scheduled(cron="*/5 * * * * MON-FRI")

By default spring batch DataSource uses the HSQL. Here I am using JPA and set the database platform (like MySQL) to point to a specific database (other than the default HSQL).

I am also using gradle wrapper (http://www.gradle.org/docs/current/userguide/gradle_wrapper.html). The wrapper is something you should check into version control. By distributing the wrapper with your project, anyone can work with it without needing to install Gradle beforehand.
Add following to build.gradle
* task wrapper(type: Wrapper) {
    gradleVersion = '1.11'
}
Command to create gradlew:-
* gradle wrapper

I am also using Spring Boot (http://projects.spring.io/spring-boot/). This helps to create a stand-along spring app - just run the app. To make the app spring-boot add the following line to build.gradle
 * apply plugin: 'spring-boot'
 * following Dependencies:-
 ** compile 'org.springframework.boot:spring-boot-starter-batch:1.1.5.RELEASE'
    
 ** compile 'org.springframework.boot:spring-boot-starter-data-jpa:1.1.5.RELEASE'

Command to spring boot
 * gradlew bootRun
 * gradlew run

DDL:-
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

CREATE TABLE `account` (
  `accountId` bigint(20) NOT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`accountId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `account`
(`accountId`,
`firstName`,
`lastName`)
VALUES
(1,'first name','last name');



