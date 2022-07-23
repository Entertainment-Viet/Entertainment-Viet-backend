package com.EntertainmentViet.backend.features.security.boundary;

public interface ResourceAuthorizationBoundary<T> {
  void authorizeRequests(T http) throws Exception;
}
