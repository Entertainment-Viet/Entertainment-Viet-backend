package com.EntertainmentViet.backend.features.booking.api.category;

import com.EntertainmentViet.backend.features.booking.boundary.category.CategoryBoundary;
import com.EntertainmentViet.backend.features.booking.dto.category.CreateCategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.category.ReadCategoryDto;
import com.EntertainmentViet.backend.features.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

  @GetMapping
  public CompletableFuture<List<ReadCategoryDto>> findAll() {
    return CompletableFuture.completedFuture(categoryService.findAll());
  }

  @GetMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<ReadCategoryDto>> findByUid(@PathVariable("uid") UUID uid) {
    return CompletableFuture.completedFuture(categoryService.findByUid(uid)
        .map( categoryDto -> ResponseEntity
            .ok()
            .body(categoryDto)
        )
        .orElse( ResponseEntity.notFound().build())
    );
  }

  @PostMapping
  public CompletableFuture<ResponseEntity<UUID>> create(HttpServletRequest request, @RequestBody @Valid CreateCategoryDto createCategoryDto) {
    return CompletableFuture.completedFuture(categoryService.create(createCategoryDto)
        .map(newCategoryUid -> ResponseEntity
            .created(RestUtils.getCreatedLocationUri(request, newCategoryUid))
            .body(newCategoryUid))
        .orElse(ResponseEntity.badRequest().build())
    );
  }

  @DeleteMapping(value = "/{uid}")
  public CompletableFuture<ResponseEntity<Void>> delete(@PathVariable("uid") UUID uid) {
    if (categoryService.delete(uid)) {
      return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }
    return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
  }
}
