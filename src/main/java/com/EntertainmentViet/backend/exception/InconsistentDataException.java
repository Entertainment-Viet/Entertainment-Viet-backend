package com.EntertainmentViet.backend.exception;

public class InconsistentDataException extends RuntimeException {
  public InconsistentDataException() {
    super("Rollback database operation due to the database inconsistent state");
  }
}
