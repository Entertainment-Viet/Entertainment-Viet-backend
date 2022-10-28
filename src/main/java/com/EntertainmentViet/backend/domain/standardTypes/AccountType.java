package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;

public enum AccountType {
  INDIVIDUAL("account.type.individual"),
  CORPORATION("account.type.corporation"),
  ;

  private static final Map<String, AccountType> BY_I18N_KEY = new HashMap<>();

  static {
    for (AccountType accountType : values()) {
      BY_I18N_KEY.put(accountType.i18nKey, accountType);
    }
  }

  public static AccountType ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  AccountType(String i18nKey) {
    this.i18nKey = i18nKey;
  }

}
