package com.sid.springbatch;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.boot.SpringApplication;

@EnableScheduling
public class JobRunner {

  @Scheduled(fixedRate = 5000)
  public void findAndRunJob() {
    SpringApplication.run(BatchConfiguration.class);
  }
}
