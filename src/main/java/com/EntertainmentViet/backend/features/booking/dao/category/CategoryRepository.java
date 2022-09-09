package com.EntertainmentViet.backend.features.booking.dao.category;

import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface CategoryRepository extends IdentifiableRepository<Category> {
}
