package com.momo.theta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThetaExampleSpringBootApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(ThetaExampleSpringBootApplication.class);
    application.run(args);
  }
}
