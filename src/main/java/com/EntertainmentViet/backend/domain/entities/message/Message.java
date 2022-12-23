package com.EntertainmentViet.backend.domain.entities.message;

import com.EntertainmentViet.backend.domain.entities.Account;
import com.EntertainmentViet.backend.domain.entities.admin.Feedback_;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;

import javax.persistence.*;
import java.time.OffsetDateTime;

//@SuperBuilder
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
public class Message {

  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = UserInputText_.INPUT_LANG, column = @Column(name = Feedback_.CONTENT + "_" + UserInputText_.INPUT_LANG)),
      @AttributeOverride( name = UserInputText_.RAW_INPUT, column = @Column(name = Feedback_.CONTENT + "_" + UserInputText_.RAW_INPUT)),
      @AttributeOverride( name = UserInputText_.INPUT_TRANSLATION, column = @Column(name = Feedback_.CONTENT + "_" + UserInputText_.INPUT_TRANSLATION))
  })
  private UserInputText content;

  private OffsetDateTime sentAt;

  private Account account;
}
