package com.EntertainmentViet.backend.features.finance.boundary;

import com.EntertainmentViet.backend.domain.entities.finance.FinanceConfig;

public interface UserDealFeeRateBoundary {
  FinanceConfig updateConfigOnOrganizerDealFee(Long organizerId, FinanceConfig financeConfig);
  FinanceConfig updateConfigOnTalentDealFee(Long talentId, FinanceConfig financeConfig);
}
