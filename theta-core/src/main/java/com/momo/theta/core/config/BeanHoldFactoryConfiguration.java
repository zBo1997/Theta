package com.momo.theta.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanHoldFactoryConfiguration {

    /**
     * 默认自动注入 SpringContextHolder
     * @return
     */
    @Bean
    public SpringContextHolder beanHoldFactory() {
        return new SpringContextHolder();
    }

}

