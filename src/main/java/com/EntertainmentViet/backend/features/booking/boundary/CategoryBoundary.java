package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.features.booking.dto.CategoryDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryBoundary {

  List<CategoryDto> findAll();

  Optional<CategoryDto> findByUid(UUID uid);
}
