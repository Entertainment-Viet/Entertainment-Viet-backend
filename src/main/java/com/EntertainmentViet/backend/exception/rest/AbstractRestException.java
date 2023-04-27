package com.EntertainmentViet.backend.exception.rest;

public abstract class AbstractRestException extends RuntimeException {
  protected AbstractRestException() {
    super();
  }

  protected AbstractRestException(String message) {
    super(message);
  }

  protected AbstractRestException(String message, Throwable ex) {
    super(message, ex);
  }
}
