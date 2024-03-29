package com.EntertainmentViet.backend.features.finance.boundary;

import com.EntertainmentViet.backend.domain.entities.finance.FinanceConfig;
import com.EntertainmentViet.backend.domain.entities.finance.UserDealFeeRate;
import com.EntertainmentViet.backend.features.common.dao.AccountRepository;
import com.EntertainmentViet.backend.features.finance.dao.UserDealFeeRateRepository;
import com.EntertainmentViet.backend.features.finance.dto.UserDealFeeRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDealFeeRateService implements UserDealFeeRateBoundary {

  private final UserDealFeeRateRepository userDealFeeRateRepository;

  private final AccountRepository accountRepository;

  @Override
  public FinanceConfig updateConfigOnOrganizerDealFee(Long organizerId, FinanceConfig financeConfig) {
    if (financeConfig == null) {
      return null;
    }

    return userDealFeeRateRepository
        .findById(organizerId)
        .map(UserDealFeeRate::getFeeRate)
        .map(customFeeRate -> FinanceConfig.builder()
            .organizerFee(customFeeRate).talentFee(financeConfig.getTalentFee()).pit(financeConfig.getPit()).vat(financeConfig.getVat()).build()
        ).orElse(financeConfig);
  }

  @Override
  public FinanceConfig updateConfigOnTalentDealFee(Long talentId, FinanceConfig financeConfig) {
    if (financeConfig == null) {
      return null;
    }

    return userDealFeeRateRepository
        .findById(talentId)
        .map(UserDealFeeRate::getFeeRate)
        .map(customFeeRate -> FinanceConfig.builder()
            .organizerFee(financeConfig.getOrganizerFee()).talentFee(customFeeRate).pit(financeConfig.getPit()).vat(financeConfig.getVat()).build()
        ).orElse(financeConfig);
  }

  @Override
  public Optional<UserDealFeeRate> findByUid(UUID userUid) {
    return userDealFeeRateRepository.findByUserUid(userUid);
  }

  @Override
  @Transactional
  public Optional<UserDealFeeRate> update(UUID userUid, UserDealFeeRateDto updatedFeeRateDto) {
    var userDealFeeRateOptional = userDealFeeRateRepository.findByUserUid(userUid);
    if (userDealFeeRateOptional.isEmpty()) {
      var userDealFeeRate = UserDealFeeRate.builder()
          .user(accountRepository.findByUid(userUid).orElse(null))
          .feeRate(updatedFeeRateDto.getFeeRate())
          .build();
      return Optional.of(userDealFeeRateRepository.save(userDealFeeRate));
    }

    var userDealFeeRate = userDealFeeRateOptional.get();
    userDealFeeRate.setFeeRate(updatedFeeRateDto.getFeeRate());

    return Optional.of(userDealFeeRateRepository.save(userDealFeeRate));
  }
}
