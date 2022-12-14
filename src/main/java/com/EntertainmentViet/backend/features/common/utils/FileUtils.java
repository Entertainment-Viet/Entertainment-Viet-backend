package com.EntertainmentViet.backend.features.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

@UtilityClass
@Slf4j
public class FileUtils {

  public static final String FILENAME_PATTERN = "[a-zA-Z0-9.\\-_+]+";

  public String generateFileCode(UUID ownerUid, Boolean isPublic, String fileExtension) {
    // By default, will be always private
    if (isPublic == null) {
      isPublic = false;
    }
    // return ownerId_private_timestamp(at zone)
    return String.format("%s_%s_%s.%s", ownerUid, isPublic, Instant.now().toEpochMilli(), fileExtension);
  }

  public boolean checkIfFileAccessible(String fileCode, UUID ownerId) {
    var parts = fileCode.split("_");
    UUID ownerPart = UUID.fromString(parts[0]);
    boolean isPublic = Boolean.valueOf(parts[0]);
    if (isPublic || ownerPart.equals(ownerId)) {
      return true;
    }
    return false;
  }

  public MediaType getFileType(String fileName) {
    if (fileName == null || fileName.isBlank()) {
      return MediaType.APPLICATION_OCTET_STREAM;
    }

    String extension = FilenameUtils.getExtension(fileName);
    if (Arrays.asList("jpg", "jpeg").contains(extension)) {
      return MediaType.IMAGE_JPEG;
    }
    else if (Arrays.asList("png").contains(extension)) {
      return MediaType.IMAGE_PNG;
    }
    else if (Arrays.asList("pdf").contains(extension)) {
      return MediaType.APPLICATION_PDF;
    }
    else {
      return MediaType.APPLICATION_OCTET_STREAM;
    }
  }
}
