package com.EntertainmentViet.backend.features.security.api;

import com.EntertainmentViet.backend.features.organizer.api.OrganizerController;
import com.EntertainmentViet.backend.features.security.boundary.AuthenticationBoundary;
import com.EntertainmentViet.backend.features.security.dto.LoginRequestDto;
import com.EntertainmentViet.backend.features.security.dto.LoginResponseDto;
import com.EntertainmentViet.backend.features.security.dto.LogoutRequestDto;
import com.EntertainmentViet.backend.features.security.dto.RefreshRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@RestController
@RequestMapping(path = AuthenticationController.REQUEST_MAPPING_PATH)
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

  public static final String REQUEST_MAPPING_PATH = "/auth";

  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

  private final AuthenticationBoundary authenticationService;

  @PostMapping(path = "/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
    return authenticationService.login(REQUEST_TIMEOUT, request)
        .map( loginResponseDto -> new ResponseEntity<>(loginResponseDto, HttpStatus.OK))
        .orElse(new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));
  }

  @PostMapping(path = "/logout")
  public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
    boolean isSuccess = authenticationService.logout(REQUEST_TIMEOUT, request);
    if (!isSuccess) {
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
    return new ResponseEntity<>(null, HttpStatus.OK);
  }

  @PostMapping(path = "/refresh")
  public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody RefreshRequestDto request) {
    return authenticationService.refreshToken(REQUEST_TIMEOUT, request)
        .map( loginResponseDto -> new ResponseEntity<>(loginResponseDto, HttpStatus.OK))
        .orElse(new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED));
  }

}
