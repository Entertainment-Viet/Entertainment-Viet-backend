package com.EntertainmentViet.backend.features.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomPage<T> {
  private List<T> content;
  private CustomPageable paging;

  public CustomPage(Page<T> page) {
    this.content = page.getContent();
    this.paging = CustomPageable.builder()
        .offset(page.getPageable().getOffset())
        .totalElements(page.getTotalElements())
        .pageNumber(page.getPageable().getPageNumber())
        .pageSize(page.getPageable().getPageSize())
        .totalPages(page.getTotalPages())
        .first(page.isFirst())
        .last(page.isLast())
        .empty(page.isEmpty())
        .sorted(page.getSort().isSorted())
        .build();
  }

  @Data
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class CustomPageable {
    private Long offset;
    private Long totalElements;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Boolean first;
    private Boolean last;
    private Boolean empty;
    private Boolean sorted;
  }
}
