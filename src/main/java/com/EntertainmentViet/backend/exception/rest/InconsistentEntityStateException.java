package com.EntertainmentViet.backend.exception.rest;

public class InconsistentEntityStateException extends AbstractRestException {

  public InconsistentEntityStateException() { super();}

  public InconsistentEntityStateException(String message) {
    super(message);
  }
}
