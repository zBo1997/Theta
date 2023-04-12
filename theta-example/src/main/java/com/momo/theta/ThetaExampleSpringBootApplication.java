package com.momo.theta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
public class ThetaExampleSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ThetaExampleSpringBootApplication.class);
        application.run(args);
    }
}
