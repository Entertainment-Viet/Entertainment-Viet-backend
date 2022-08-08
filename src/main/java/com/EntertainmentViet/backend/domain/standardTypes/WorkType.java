package com.EntertainmentViet.backend.domain.standardTypes;


import java.util.HashMap;
import java.util.Map;

public enum WorkType {
  SINGLE_TIME("work.type.single-time"),
  SINGLE_SHOW("work.type.single-show"),
  PERIOD_CONTRACT("work.type.period-contract"),
  ;

  private static final Map<String, WorkType> BY_I18N_KEY = new HashMap<>();

  static {
    for (WorkType workType : values()) {
      BY_I18N_KEY.put(workType.i18nKey, workType);
    }
  }

  public static WorkType ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  WorkType(String i18nKey) {
    this.i18nKey = i18nKey;
  }
}
