package com.EntertainmentViet.backend.features.common.dao;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
@Transactional
public interface IdentifiableRepository<T extends Identifiable> extends BaseRepository<T> {
  Optional<T> findByUid(UUID id);
}
