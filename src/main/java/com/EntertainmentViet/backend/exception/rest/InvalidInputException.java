package com.EntertainmentViet.backend.exception.rest;

public class InvalidInputException extends AbstractRestException {

  public InvalidInputException() { super();}

  public InvalidInputException(String message) {
    super(message);
  }
}
