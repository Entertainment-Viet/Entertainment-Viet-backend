package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.config.properties.AwsProperties;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AwsConfig {

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
