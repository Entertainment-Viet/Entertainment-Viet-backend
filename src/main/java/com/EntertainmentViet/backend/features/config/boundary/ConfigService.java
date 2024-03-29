package com.EntertainmentViet.backend.features.config.boundary;

import com.EntertainmentViet.backend.config.constants.AppConstant;
import com.EntertainmentViet.backend.domain.entities.AppConfig;
import com.EntertainmentViet.backend.domain.entities.finance.FinanceConfig;
import com.EntertainmentViet.backend.exception.rest.WrongSystemConfigurationException;
import com.EntertainmentViet.backend.features.config.dao.ConfigRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigService implements ConfigBoundary {

  private final ConfigRepository configRepository;

  private final Gson gson;

  @Override
  public Optional<FinanceConfig> getFinance() {
    try {
      var financeConfig = configRepository.findByPropertyName(AppConstant.FINANCE_PROPERTIES);
      FinanceConfig financeConfigValue;
      if (financeConfig.isEmpty()) {
        financeConfigValue = FinanceConfig.builder().build();
      } else {
        financeConfigValue = gson.fromJson(financeConfig.get().getPropertyValue(), FinanceConfig.class);
      }
      return Optional.of(financeConfigValue);
    }
    catch (JsonSyntaxException jsonSyntaxException) {
      throw new WrongSystemConfigurationException("The finance config is not in the correct format", jsonSyntaxException);
    }
  }

  @Override
  public void configFinance(FinanceConfig financeConfig) {
    var curFinanceConfig = configRepository.findByPropertyName(AppConstant.FINANCE_PROPERTIES)
        .orElse(AppConfig.builder().propertyName(AppConstant.FINANCE_PROPERTIES).build());

    curFinanceConfig.setPropertyValue(gson.toJson(financeConfig));
    configRepository.save(curFinanceConfig);
  }
}
