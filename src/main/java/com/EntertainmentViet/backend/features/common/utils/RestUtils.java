package com.EntertainmentViet.backend.features.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.UUID;

@UtilityClass
public class RestUtils {

  public static URI getCreatedLocationUri(HttpServletRequest request, UUID id) {
    return ServletUriComponentsBuilder
        .fromRequestUri(request)
        .path("/{id}")
        .buildAndExpand(id.toString())
        .toUri();
  }
}
