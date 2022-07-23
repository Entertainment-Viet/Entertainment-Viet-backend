package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.features.security.dto.LoginInfoDto;
import com.EntertainmentViet.backend.features.security.dto.LoginTokenDto;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

public interface AuthenticationBoundary {

  void logout(HttpServletRequest request);

  LoginTokenDto login(Duration timeout, LoginInfoDto loginInfo);
}
