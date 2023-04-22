package com.EntertainmentViet.backend.features.email;

public enum EMAIL_ACTION {
  UPDATE_PASSWORD("Update Password"),
  VERIFY_EMAIL("Verify Email"),
  ;

  public final String text;

  EMAIL_ACTION(String text) {
    this.text = text;
  }
}
