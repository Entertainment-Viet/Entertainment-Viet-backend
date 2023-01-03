package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.standardTypes.AccountType;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.standardTypes.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Mapper(config = MappingConfig.class)
public class StandardTypeMapper {

  @ToUserStateKey
  public String toUserStateKey(UserState userState) {
    return userState != null ? userState.i18nKey : null;
  }

  @ToUserState
  public UserState toUserState(String i18nKey) {
    return UserState.ofI18nKey(i18nKey);
  }

  @ToAccountTypeKey
  public String toAccountTypeKey(AccountType accountType) {
    return accountType != null ? accountType.i18nKey : null;
  }

  @ToAccountType
  public AccountType toAccountType(String i18nKey) {
    return AccountType.ofI18nKey(i18nKey);
  }

  @ToUserTypeKey
  public String toUserTypeKey(UserType userType) {
    return userType != null ? userType.i18nKey : null;
  }

  @ToUserType
  public UserType toUserType(String i18nKey) {
    return UserType.ofI18nKey(i18nKey);
  }

  @ToPaymentType
  public PaymentType toPaymentType(String payment) {
    return payment != null ? PaymentType.ofI18nKey(payment) : null;
  }

  @ToPaymentTypeKey
  public String toPaymentTypeKey(PaymentType paymentType) {
    return paymentType != null ? paymentType.i18nKey : null;
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToUserStateKey {  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToUserState {  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToAccountTypeKey {  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToAccountType {  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToUserTypeKey {  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToUserType {  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToPaymentTypeKey {  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToPaymentType {  }
}
