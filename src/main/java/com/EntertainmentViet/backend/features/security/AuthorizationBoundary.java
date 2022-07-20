package com.EntertainmentViet.backend.features.security;

public interface AuthorizationBoundary<T> {
  void authorizeRequests(T http) throws Exception;
}
