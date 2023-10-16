package com.momo.theta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class ThetaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ThetaGatewayApplication.class);
        application.run(args);
    }
}
