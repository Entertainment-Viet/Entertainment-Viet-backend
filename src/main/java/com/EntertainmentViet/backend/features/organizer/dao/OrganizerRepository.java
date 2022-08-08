package com.EntertainmentViet.backend.features.organizer.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
}
