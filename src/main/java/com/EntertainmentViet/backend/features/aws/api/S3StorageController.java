package com.EntertainmentViet.backend.features.aws.api;

import com.EntertainmentViet.backend.features.aws.boundary.S3StorageBoundary;
import com.EntertainmentViet.backend.features.common.utils.FileUtils;
import com.EntertainmentViet.backend.features.common.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Async
@RequestMapping(path = S3StorageController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class S3StorageController {

  public static final String REQUEST_MAPPING_PATH = "/aws/files";

  private final S3StorageBoundary s3StorageService;

  @PostMapping
  public CompletableFuture<ResponseEntity<String>> uploadFile(JwtAuthenticationToken token,
                                            @RequestParam("public") Boolean isPublic,
                                            @RequestParam("file") MultipartFile file) {
    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    UUID ownerId = TokenUtils.getUid(token);
    String fileCode = FileUtils.generateFileCode(ownerId, isPublic, fileExtension);

    if (s3StorageService.uploadFile(fileCode, file)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok(fileCode));
    }
    return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @GetMapping("/{fileCode}")
  public CompletableFuture<ResponseEntity<byte[]>> getFile(JwtAuthenticationToken token, @PathVariable("fileCode") String fileCode) throws IOException {
    UUID ownerId = TokenUtils.getUid(token);
    boolean isAccessible = FileUtils.checkIfFileAccessible(fileCode, ownerId);
    if (!isAccessible) {
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    MediaType contentType = FileUtils.getFileType(fileCode);
    return CompletableFuture.completedFuture(ResponseEntity.ok()
        .contentType(contentType)
        .body(s3StorageService.getFile(fileCode).readAllBytes())
    );
  }

  @DeleteMapping("/{fileCode}")
  public CompletableFuture<ResponseEntity<Boolean>> removeFile(JwtAuthenticationToken token, @PathVariable("fileCode") String fileCode) {
    UUID ownerId = TokenUtils.getUid(token);
    boolean isAccessible = FileUtils.checkIfFileAccessible(fileCode, ownerId);
    if (!isAccessible) {
      return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.ok(s3StorageService.deleteFile(fileCode)));
  }
}
