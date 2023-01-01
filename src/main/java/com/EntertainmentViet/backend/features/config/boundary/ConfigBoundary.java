package com.EntertainmentViet.backend.features.config.boundary;

import com.EntertainmentViet.backend.domain.entities.config.FinanceConfig;

import java.util.Optional;

public interface ConfigBoundary {
  Optional<FinanceConfig> getFinance();

  void configFinance(FinanceConfig financeConfig);
}
