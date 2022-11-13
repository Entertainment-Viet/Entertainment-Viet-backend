package com.EntertainmentViet.backend.features.booking.dao.category;

import javax.transaction.Transactional;

import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CategoryRepository extends IdentifiableRepository<Category> {
}
