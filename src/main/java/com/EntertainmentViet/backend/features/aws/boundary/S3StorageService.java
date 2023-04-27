package com.EntertainmentViet.backend.features.aws.boundary;

import com.EntertainmentViet.backend.config.properties.AwsProperties;
import com.EntertainmentViet.backend.exception.rest.SystemUnavailableException;
import com.EntertainmentViet.backend.exception.rest.WrongSystemConfigurationException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3StorageService implements S3StorageBoundary {

  private final AmazonS3 amazonS3Client;

  private final AwsProperties awsProperties;

  @Override
  public boolean uploadFile(String fileCode, MultipartFile file) {
    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(file.getSize());
      amazonS3Client.putObject(awsProperties.getBucketName(), fileCode, file.getInputStream(), metadata);
      log.info(String.format("File %s uploaded" ,fileCode));
      return true;
    } catch (IOException ioe) {
      throw new WrongSystemConfigurationException(String.format("Can not upload file %s to cloud", fileCode), ioe);
    } catch (AmazonServiceException serviceException) {
      log.info("AmazonServiceException happened. File not uploaded: " + fileCode);
      throw new SystemUnavailableException("Can not connect to cloud service", serviceException);
    } catch (AmazonClientException clientException) {
      log.info("AmazonClientException happened. File not uploaded: " + fileCode);
      throw new SystemUnavailableException("Can not connect to cloud service", clientException);
    }
  }

  @Override
  public boolean deleteFile(final String fileCode) {
    // TODO: adding checking fileCode with owner id
    amazonS3Client.deleteObject(awsProperties.getBucketName(), fileCode);
    log.info("Deleted File: " + fileCode);
    return true;
  }

  @Override
  public InputStream getFile(String fileCode) {
    // TODO: adding checking fileCode with owner id

    try {
      S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(awsProperties.getBucketName(), fileCode));
      return s3object.getObjectContent();
    } catch (AmazonServiceException serviceException) {
      log.info("AmazonServiceException Message:    " + serviceException.getMessage());
      throw serviceException;
    } catch (AmazonClientException clientException) {
      log.info("AmazonClientException Message: " + clientException.getMessage());
      throw clientException;
    }
  }

  @Override
  public List<String> listFiles() {

    ListObjectsRequest listObjectsRequest =
        new ListObjectsRequest()
            .withBucketName(awsProperties.getBucketName());

    List<String> keys = new ArrayList<>();

    ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);

    while (true) {
      var objectSummaries = objects.getObjectSummaries();
      if (objectSummaries.size() < 1) {
        break;
      }

      for (S3ObjectSummary item : objectSummaries) {
        if (!item.getKey().endsWith("/"))
          keys.add(item.getKey());
      }

      objects = amazonS3Client.listNextBatchOfObjects(objects);
    }

    return keys;
  }
}
