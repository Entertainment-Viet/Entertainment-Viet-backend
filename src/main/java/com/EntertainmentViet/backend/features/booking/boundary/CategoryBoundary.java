package com.EntertainmentViet.backend.features.booking.boundary;

import com.EntertainmentViet.backend.features.booking.dto.CategoryDto;

import java.util.List;

public interface CategoryBoundary {

  List<CategoryDto> findAll();

  CategoryDto findById(Long id);
}
