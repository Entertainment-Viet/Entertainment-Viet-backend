package com.EntertainmentViet.backend.config;

import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.time.Duration;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

  @Bean
  public ThreadPoolTaskExecutor asyncFixedThreadPoolTaskExecutor() {
    int threadCount = Runtime.getRuntime().availableProcessors() * 2;

    return new TaskExecutorBuilder()
        .corePoolSize(threadCount)
        .maxPoolSize(threadCount)
        .keepAlive(Duration.ZERO)
        .threadNamePrefix("AsyncRequestThread-")
        .build();
  }

  @Bean(name = AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
  @Override
  public AsyncTaskExecutor getAsyncExecutor() {
    return new DelegatingSecurityContextAsyncTaskExecutor(asyncFixedThreadPoolTaskExecutor());
  }
}
