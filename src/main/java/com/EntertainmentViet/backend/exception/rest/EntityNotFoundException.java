package com.EntertainmentViet.backend.exception.rest;

import java.util.UUID;

public class EntityNotFoundException extends AbstractRestException {
  public EntityNotFoundException(String entityType, UUID uuid) {
    super(String.format("Can not find any satisfied %s at uid '%s'", entityType, uuid));
  }
}
