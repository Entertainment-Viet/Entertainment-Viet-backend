package com.EntertainmentViet.backend.features.finance.boundary;

import com.EntertainmentViet.backend.domain.entities.finance.FinanceConfig;
import com.EntertainmentViet.backend.domain.entities.finance.UserDealFeeRate;
import com.EntertainmentViet.backend.features.finance.dto.UserDealFeeRateDto;

import java.util.Optional;
import java.util.UUID;

public interface UserDealFeeRateBoundary {
  FinanceConfig updateConfigOnOrganizerDealFee(Long organizerId, FinanceConfig financeConfig);
  FinanceConfig updateConfigOnTalentDealFee(Long talentId, FinanceConfig financeConfig);

  Optional<UserDealFeeRate> findByUid(UUID userUid);
  Optional<UserDealFeeRate> update(UUID userUid, UserDealFeeRateDto updatedFeeRate);
}
