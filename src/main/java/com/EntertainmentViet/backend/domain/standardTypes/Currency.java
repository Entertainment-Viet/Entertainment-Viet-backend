package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;

public enum Currency {
  VND("currency.vnd"),
  USD("currency.usd"),
  EUR("currency.eur"),
  ;

  private static final Map<String, Currency> BY_I18N_KEY = new HashMap<>();

  static {
    for (Currency currency : values()) {
      BY_I18N_KEY.put(currency.i18nKey, currency);
    }
  }

  public static Currency ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  Currency(String i18nKey) {
    this.i18nKey = i18nKey;
  }
}
