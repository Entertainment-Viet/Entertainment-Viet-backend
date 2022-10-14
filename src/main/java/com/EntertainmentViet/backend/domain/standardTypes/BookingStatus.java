package com.EntertainmentViet.backend.domain.standardTypes;

import java.util.HashMap;
import java.util.Map;

public enum BookingStatus {
  ORGANIZER_PENDING("booking.status.organizer-pending"),
  TALENT_PENDING("booking.status.talent-pending"),
  CONFIRMED("booking.status.confirmed"),
  CANCELLED("booking.status.cancelled"),
  FINISHED("booking.status.finished"),
  ORGANIZER_FINISH("booking.status.organizer-finished"),
  TALENT_FINISH("booking.status.talent-finished"),
  ARCHIVED("booking.status.archived"),
  ;

  private static final Map<String, BookingStatus> BY_I18N_KEY = new HashMap<>();

  static {
    for (BookingStatus bookingStatus : values()) {
      BY_I18N_KEY.put(bookingStatus.i18nKey, bookingStatus);
    }
  }

  public static BookingStatus ofI18nKey(String i18n) {
    return BY_I18N_KEY.get(i18n);
  }

  public final String i18nKey;

  BookingStatus(String i18nKey) {
    this.i18nKey = i18nKey;
  }

}
