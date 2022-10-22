package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;


public enum UserState {
  GUEST("user.state.guest"),
  PENDING("user.state.pending"),
  VERIFIED("user.state.verified"),
  CHARGEABLE("user.state.chargeable"),
  ARCHIVED("user.state.archived"),
  ;

  private static final Map<String, UserState> BY_I18N_KEY = new HashMap<>();

  static {
    for (UserState userState : values()) {
      BY_I18N_KEY.put(userState.i18nKey, userState);
    }
  }

  public static UserState ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }


  public final String i18nKey;

  UserState(String i18nKey) {
    this.i18nKey = i18nKey;
  }
}
