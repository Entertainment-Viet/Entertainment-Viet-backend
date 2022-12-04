package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.config.properties.AwsProperties;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AwsConfig implements AsyncConfigurer {

  private final AwsProperties awsProperties;

  @Bean
  public AmazonS3 amazonS3Client() {

    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsProperties.getAwsId(), awsProperties.getAwsKey());
    AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.fromName(awsProperties.getRegion()))
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();

    return amazonS3Client;
  }
}
