package com.EntertainmentViet.backend.features.booking.api;

import com.EntertainmentViet.backend.features.booking.boundary.CategoryBoundary;
import com.EntertainmentViet.backend.features.booking.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = CategoryController.REQUEST_MAPPING_PATH)
public class CategoryController {

  public static final String REQUEST_MAPPING_PATH = "/categories";

  private final CategoryBoundary categoryService;

  @GetMapping()
  public List<CategoryDto> findAll() {
    return categoryService.findAll();
  }

}
