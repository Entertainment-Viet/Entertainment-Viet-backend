package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.features.booking.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService implements CategoryBoundary {

  @Override
  public List<CategoryDto> findAll() {
    return Arrays.asList(CategoryDto.builder().name("Hiphop").parentName("Dance").build());
  }

  @Override
  public CategoryDto findById(Long id) {
    return CategoryDto.builder().name("Dance").build();
  }
}
