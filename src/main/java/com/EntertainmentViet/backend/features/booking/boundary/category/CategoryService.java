package com.EntertainmentViet.backend.features.booking.boundary.category;

import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import com.EntertainmentViet.backend.features.booking.dao.category.CategoryRepository;
import com.EntertainmentViet.backend.features.booking.dto.category.CategoryMapper;
import com.EntertainmentViet.backend.features.booking.dto.category.CreateCategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.category.ReadCategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService implements CategoryBoundary {

  private final CategoryMapper categoryMapper;

  private final CategoryRepository categoryRepository;

  @Override
  public List<ReadCategoryDto> findAll() {
    return categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toList());
  }

  @Override
  public Optional<ReadCategoryDto> findByUid(UUID uid) {
    return Optional.ofNullable(categoryMapper.toDto(categoryRepository.findByUid(uid).orElse(null)));
  }

  @Override
  public Optional<UUID> create(CreateCategoryDto createCategoryDto) {
    var newCategory = categoryMapper.toModel(createCategoryDto);
    if (newCategory.getParent() == null && createCategoryDto.getParentUid() != null) {
      throw new InvalidInputException(String.format("Can not find corresponding parent category with uid %s when creating new child category",
          createCategoryDto.getParentUid()));
    }

    return Optional.ofNullable(categoryRepository.save(newCategory).getUid());
  }

  @Override
  public boolean delete(UUID categoryUid) {
    var category = categoryRepository.findByUid(categoryUid);

    if (category.isEmpty()) {
      return false;
    }

    categoryRepository.delete(category.get());
    return true;
  }
}
