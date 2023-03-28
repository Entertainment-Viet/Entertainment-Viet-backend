package com.EntertainmentViet.backend.features.booking.boundary.category;

import com.EntertainmentViet.backend.features.booking.dto.category.CreateCategoryDto;
import com.EntertainmentViet.backend.features.booking.dto.category.ReadCategoryDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryBoundary {

  List<ReadCategoryDto> findAll();

  Optional<ReadCategoryDto> findByUid(UUID uid);

  Optional<UUID> create(CreateCategoryDto createCategoryDto);

  boolean delete(UUID categoryUid);
}
