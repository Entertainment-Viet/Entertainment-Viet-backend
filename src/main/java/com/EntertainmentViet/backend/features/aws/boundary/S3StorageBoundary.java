package com.EntertainmentViet.backend.features.aws.boundary;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface S3StorageBoundary {

  boolean uploadFile(String fileCode, MultipartFile file);

  boolean deleteFile(String fileCode);

  InputStream getFile(String fileCode);

  List<String> listFiles();
}
