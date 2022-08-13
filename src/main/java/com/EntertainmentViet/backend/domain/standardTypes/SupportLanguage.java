package com.EntertainmentViet.backend.domain.standardTypes;


import java.util.HashMap;
import java.util.Map;

public enum SupportLanguage {
  ENGLISH("language.en"),
  VIETNAMESE("language.vn"),
  GERMAN("language.de"),
  FRANCE("language.fr"),
  ;

  private static final Map<String, SupportLanguage> BY_I18N_KEY = new HashMap<>();

  static {
    for (SupportLanguage workType : values()) {
      BY_I18N_KEY.put(workType.i18nKey, workType);
    }
  }

  public static SupportLanguage ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  SupportLanguage(String i18nKey) {
    this.i18nKey = i18nKey;
  }
}
