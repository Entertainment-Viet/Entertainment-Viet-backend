package com.EntertainmentViet.backend.features.booking.api.category;

import com.EntertainmentViet.backend.features.booking.boundary.category.CategoryBoundary;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Async
@Validated
@RequestMapping(path = CategoryController.REQUEST_MAPPING_PATH)
public class CategoryController {

  public static final String REQUEST_MAPPING_PATH = "/categories";

  private final CategoryBoundary categoryService;

  @GetMapping()
  public CompletableFuture<List<CategoryDto>> findAll() {
    return CompletableFuture.completedFuture(categoryService.findAll());
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<CategoryDto>> findAll(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(categoryService.findByUid(uid)
        .map( categoryDto -> ResponseEntity
            .ok()
            .body(categoryDto)
        )
        .orElse( ResponseEntity.notFound().build())
    );  }

}
