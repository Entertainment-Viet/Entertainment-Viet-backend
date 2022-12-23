package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
  INDIVIDUAL("user.type.individual"),
  CORPORATION("user.type.corporation"),
  ;

  private static final Map<String, UserType> BY_I18N_KEY = new HashMap<>();

  static {
    for (UserType userType : values()) {
      BY_I18N_KEY.put(userType.i18nKey, userType);
    }
  }

  public static UserType ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  UserType(String i18nKey) {
    this.i18nKey = i18nKey;
  }

}
