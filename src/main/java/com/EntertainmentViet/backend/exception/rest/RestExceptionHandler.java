package com.EntertainmentViet.backend.exception.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InconsistentEntityStateException.class)
  public ResponseEntity<ApiError> handleInvalidEntityStateException(InconsistentEntityStateException ex, WebRequest request) {
    log.error(String.format("Catch %s error when process api %s.\n  >>> %s",
        ex.getClass(), request.getDescription(false), ex.getMessage()));
    return buildError(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<ApiError> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
    log.error(String.format("Catch %s error when process api %s.\n  >>> %s",
        ex.getClass(), request.getDescription(false), ex.getMessage()));
    return buildError(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
    log.error(String.format("Catch %s error when process api %s.\n  >>> %s",
        ex.getClass(), request.getDescription(false), ex.getMessage()));
    return buildError(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(UnauthorizedTokenException.class)
  public ResponseEntity<ApiError> handleUnauthorizedTokenException(UnauthorizedTokenException ex, WebRequest request) {
    log.error(String.format("Don't have enough permission to call api %s.\n  >>> %s",
        request.getDescription(false), ex.getMessage()));
    return buildError(HttpStatus.UNAUTHORIZED, ex);
  }

  @ExceptionHandler(WrongSystemConfigurationException.class)
  public ResponseEntity<ApiError> handleWrongSystemConfigurationException(WrongSystemConfigurationException ex, WebRequest request) {
    log.error(String.format("Get system %s error when process api %s.\n  >>> %s",
        ex.getClass(), request.getDescription(false), ex.getMessage()));
    return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
  }

  @ExceptionHandler(SystemUnavailableException.class)
  public ResponseEntity<ApiError> handleSystemUnavailableException(SystemUnavailableException ex, WebRequest request) {
    log.error(String.format("Get system %s error when process api %s.\n  >>> %s",
        ex.getClass(), request.getDescription(false), ex.getMessage()));
    return buildError(HttpStatus.SERVICE_UNAVAILABLE, ex);
  }

  private ResponseEntity<ApiError> buildError(HttpStatus httpStatus, AbstractRestException error) {
    error.printStackTrace();

    var apiError = ApiError.builder()
        .error(error != null ? error.getClass().getSimpleName() : null)
        .description(error != null ? error.getMessage() : null)
        .timestamp(ZonedDateTime.now())
        .status(httpStatus)
        .build();

    return new ResponseEntity<>(apiError, httpStatus);
  }
}
