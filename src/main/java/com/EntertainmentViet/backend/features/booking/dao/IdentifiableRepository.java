package com.EntertainmentViet.backend.features.booking.dao;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoRepositoryBean
public interface IdentifiableRepository <T extends Identifiable> extends JpaRepository<T, Long> {
  Logger log = LoggerFactory.getLogger(IdentifiableRepository.class);

  @Transactional
  default T saveWithUuid(T entity) {
    setUuid(entity);
    return save(entity);
  }

  @Transactional
  default <S extends T> List<S> saveAllWithUuid(Collection<S> entities) {
    List<S> entitiesToSave = excludeExisting(entities)
        .map(entity -> {
          this.setUuid(entity);
          return entity;
        })
        .collect(Collectors.toList());

    return saveAll(entitiesToSave);
  }

  default <S extends T> Stream<S> excludeExisting(Collection<S> entities) {
    // use a single query to minimize database calls
    List<UUID> uuids = entities.stream()
        .map(Identifiable::getUid)
        .collect(Collectors.toList());
    var existingIds = findAllByUid(uuids)
        .stream()
        .map(Identifiable::getUid)
        .collect(Collectors.toSet());

    return entities.stream()
        .filter(entity -> {
          var id = entity.getUid();
          if (id != null && existingIds.contains(id)) {
            log.warn("{} with id [{}] already exists, skipping insertion.", entity.getClass().getSimpleName(), id);
            return false;
          }
          return true;
        });
  }

  // TODO: Implement with QueryDSL
  default <S extends T> Collection<S> findAllByUid(List<UUID> uuids) {
    return null;
  }

  default void setUuid(T entity) {
    if (entity.getUid() == null) {
      entity.setUid(UUID.randomUUID());
    }
  }
}
