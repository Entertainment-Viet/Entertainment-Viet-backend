package com.EntertainmentViet.backend.exception.rest;

public class UnauthorizedTokenException extends AbstractRestException {

  public UnauthorizedTokenException() { super();}

  public UnauthorizedTokenException(String message) {
    super(message);
  }
}
