package com.EntertainmentViet.backend.features.booking.dao;

import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CategoryRepository extends IdentifiableRepository<Category> {
}