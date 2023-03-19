package com.EntertainmentViet.backend.features.common.dao;

import com.EntertainmentViet.backend.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByUid(UUID uid);
}
