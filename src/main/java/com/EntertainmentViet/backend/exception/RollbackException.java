package com.EntertainmentViet.backend.exception;

public class RollbackException extends RuntimeException {
  public RollbackException() {
    super("Rollback db due to exception in service");
  }
}
