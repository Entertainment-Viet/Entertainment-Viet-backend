package com.EntertainmentViet.backend.exception.rest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WrongSystemConfigurationException extends AbstractRestException {

  public WrongSystemConfigurationException() { super();}

  public WrongSystemConfigurationException(String message) {
    super(message + ". Please contact system administrator");
  }

  public WrongSystemConfigurationException(String message, Throwable e) {
    super(message + ". Please contact system administrator", e);
  }
}
