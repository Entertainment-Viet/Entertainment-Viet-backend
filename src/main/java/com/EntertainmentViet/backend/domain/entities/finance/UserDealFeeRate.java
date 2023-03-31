package com.EntertainmentViet.backend.domain.entities.finance;

import com.EntertainmentViet.backend.domain.entities.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserDealFeeRate {

  @Id
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private Account user;

  private Double feeRate;
}
