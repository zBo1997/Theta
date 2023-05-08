package com.momo.theta.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @ClassName: AsyncTaskPoolConfig
 * @Description:
 * @Author: momo
 * @Date: 2023-04-09 19:23
 **/
@Configuration
@EnableAsync
public class AsyncTaskPoolConfig {

  @Bean("exportTaskExecutor")
  public Executor taskExecutor() {
    int i = Runtime.getRuntime().availableProcessors();
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(i);
    taskExecutor.setMaxPoolSize(i);
    taskExecutor.setQueueCapacity(Integer.MAX_VALUE);
    taskExecutor.setKeepAliveSeconds(60);
    taskExecutor.setThreadNamePrefix("exportTaskExecutor--");
    taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    taskExecutor.setAwaitTerminationSeconds(60);
    return taskExecutor;
  }
}
