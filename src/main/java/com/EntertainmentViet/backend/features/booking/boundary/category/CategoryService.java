package com.EntertainmentViet.backend.features.booking.boundary.category;

import com.EntertainmentViet.backend.features.booking.dao.category.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryBoundary {

  private final CategoryMapper categoryMapper;

  private final CategoryRepository categoryRepository;

  @Override
  public List<CategoryDto> findAll() {
    return categoryRepository.findAll().stream().map(categoryMapper::toDto).toList();
  }

  @Override
  public Optional<CategoryDto> findByUid(UUID uid) {
    return Optional.ofNullable(categoryMapper.toDto(categoryRepository.findByUid(uid).orElse(null)));
  }
}
