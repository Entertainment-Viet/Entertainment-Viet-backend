package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;

public enum PackageType {
  ONCE("package.type.once"),
  RECURRING("package.type.recurring"),
  ;

  private static final Map<String, PackageType> BY_I18N_KEY = new HashMap<>();

  static {
    for (PackageType packageType : values()) {
      BY_I18N_KEY.put(packageType.i18nKey, packageType);
    }
  }

  public static PackageType ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  PackageType(String i18nKey) {
    this.i18nKey = i18nKey;
  }

}
