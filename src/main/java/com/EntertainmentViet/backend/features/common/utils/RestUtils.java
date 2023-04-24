package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.features.common.dto.CustomPage;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class RestUtils {

  public <T> Page<T> getPageEntity(List<T> list, Pageable pageable) {
    var offset = getPagingOffer(list, pageable);
    return new PageImpl<>(list.subList(offset.getStart(), offset.getEnd()), pageable, list.size());
  }

  // For lazy loading in Application level only
  public <T> CustomPage<T> toPageResponse(Page<T> page) {
    return new CustomPage<>(page);
  }


  // For lazy loading up to DB level
  public <T> CustomPage<T> toLazyLoadPageResponse(Page<T> page) {
    var lazyLoadCustomPage = new CustomPage<>(page);
    lazyLoadCustomPage.getPaging().setTotalPages(null);
    lazyLoadCustomPage.getPaging().setTotalElements(null);

    return lazyLoadCustomPage;
  }

  public URI getCreatedLocationUri(HttpServletRequest request, UUID id) {
    return ServletUriComponentsBuilder
        .fromRequestUri(request)
        .path("/{id}")
        .buildAndExpand(id.toString())
        .toUri();
  }

  public PagingOffset getPagingOffer(List list, Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), list.size());

    if (start <= end) {
      return PagingOffset.builder().start(start).end(end).build();
    }
    // If start > end, return nothing
    else {
      return PagingOffset.builder().start(0).end(0).build();
    }
  }

  @Builder
  @Value
  public static class PagingOffset {
    int start;
    int end;
  }
}
