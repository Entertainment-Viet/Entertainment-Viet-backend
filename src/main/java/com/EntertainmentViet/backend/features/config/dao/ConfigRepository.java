package com.EntertainmentViet.backend.features.config.dao;

import com.EntertainmentViet.backend.domain.entities.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<AppConfig, Long> {

  Optional<AppConfig> findByPropertyName(String propertyName);
}
