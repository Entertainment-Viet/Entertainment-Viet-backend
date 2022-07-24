package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.features.security.dto.LoginRequestDto;
import com.EntertainmentViet.backend.features.security.dto.LoginResponseDto;
import com.EntertainmentViet.backend.features.security.dto.LogoutRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Optional;

public interface AuthenticationBoundary {

  void logout(Duration timeout, LogoutRequestDto request);

  Optional<LoginResponseDto> login(Duration timeout, LoginRequestDto request);
}
