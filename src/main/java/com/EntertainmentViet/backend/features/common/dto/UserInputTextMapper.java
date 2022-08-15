package com.EntertainmentViet.backend.features.common.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.standardTypes.SupportLanguage;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Mapper(config = MappingConfig.class)
public abstract class UserInputTextMapper {

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToTranslatedText {
  }

  @Qualifier
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface ToUserInputTextObject {
  }

  @ToTranslatedText
  public String toTranslatedText(UserInputText userInputText) {
    // TODO implement translation
    return userInputText != null ? userInputText.getRawInput() : "";
  }

  @ToUserInputTextObject
  public UserInputText toUserInputTextObject(String inputText) {
    // TODO implement translation
    return UserInputText.builder().inputLang(SupportLanguage.VIETNAMESE).rawInput(inputText).build();
  }
}
