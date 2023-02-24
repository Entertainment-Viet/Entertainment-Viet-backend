package com.EntertainmentViet.backend.config.constants;

public interface AppConstant {
  String FINANCE_PROPERTIES = "finance";
  String FINANCE_TALENT_FEE = "talent-fee";
  String FINANCE_ORGANIZER_FEE = "organizer-fee";
  String FINANCE_VAT = "vat";
  String FINANCE_PIT = "pit";

  String NOTIFICATION_BASE_TOPIC = "/topic";
  String NOTIFICATION_LAST_READ_TOPIC = NOTIFICATION_BASE_TOPIC + "/last-read";
  String NOTIFICATION_LAST_BOOKING_TOPIC = NOTIFICATION_BASE_TOPIC + "/booking";
}
