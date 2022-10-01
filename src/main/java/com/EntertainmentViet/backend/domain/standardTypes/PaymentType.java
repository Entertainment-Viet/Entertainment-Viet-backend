package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;

public enum PaymentType {
  ONLINE("payment.online"),
  OFFLINE("payment.offline"),
  ;

  private static final Map<String, PaymentType> BY_I18N_KEY = new HashMap<>();

  static {
    for (PaymentType paymentType : values()) {
      BY_I18N_KEY.put(paymentType.i18nKey, paymentType);
    }
  }

  public static PaymentType ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  PaymentType(String i18nKey) {
    this.i18nKey = i18nKey;
  }

}
