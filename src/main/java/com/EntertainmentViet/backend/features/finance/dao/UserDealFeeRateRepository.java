package com.EntertainmentViet.backend.features.finance.dao;

import com.EntertainmentViet.backend.domain.entities.finance.UserDealFeeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDealFeeRateRepository extends JpaRepository<UserDealFeeRate, Long> {

  Optional<UserDealFeeRate> findByUserUid(UUID uuid);
}
