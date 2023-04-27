package com.EntertainmentViet.backend.exception.rest;

public class SystemUnavailableException extends AbstractRestException {

  public SystemUnavailableException() { super();}

  public SystemUnavailableException(String message) {
    super(message + ". Please try again later");
  }

  public SystemUnavailableException(String message, Throwable e) {
    super(message + ". Please try again later", e);
  }
}
