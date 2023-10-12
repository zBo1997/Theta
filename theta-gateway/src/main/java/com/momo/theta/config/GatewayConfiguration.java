package com.momo.theta.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class GatewayConfiguration implements InitializingBean {

    @Value("${test:1}")
    private String test;


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("我被加载了");
        System.out.println(test);
    }
}
