package com.momo.theta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanHoldFactoryConfiguration {

  /**
   * 默认自动注入 SpringContextHolder
   *
   * @return 返回自定义的Spring上下文
   */
  @Bean
  public SpringContextHolder beanHoldFactory() {
    return new SpringContextHolder();
  }

}

