package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.booking.dao.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.CategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryBoundary {

  private final CategoryMapper categoryMapper;

  private final CategoryRepository categoryRepository;

  @Override
  public List<CategoryDto> findAll() {
    return categoryRepository.findAll().stream().map(categoryMapper::toDto).toList();
  }
}
