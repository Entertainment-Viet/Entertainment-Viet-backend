package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;

public enum FeedbackStatus {
  NEW("feedback.status.new"),
  IN_PROGRESS("feedback.status.in-progress"),
  SOLVED("feedback.status.solved"),
  REJECT("feedback.status.reject"),
  ;

  private static final Map<String, FeedbackStatus> BY_I18N_KEY = new HashMap<>();

  static {
    for (FeedbackStatus bookingStatus : values()) {
      BY_I18N_KEY.put(bookingStatus.i18nKey, bookingStatus);
    }
  }

  public static FeedbackStatus ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  FeedbackStatus(String i18nKey) {
    this.i18nKey = i18nKey;
  }

}
