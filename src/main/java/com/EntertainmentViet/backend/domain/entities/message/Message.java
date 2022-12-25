package com.EntertainmentViet.backend.domain.entities.message;

import com.EntertainmentViet.backend.domain.entities.Account;
import com.EntertainmentViet.backend.domain.values.Location_;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import com.querydsl.core.annotations.QueryInit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Message {

  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = UserInputText_.INPUT_LANG, column = @Column(name = Message_.CONTENT + "_" + UserInputText_.INPUT_LANG)),
      @AttributeOverride( name = UserInputText_.RAW_INPUT, column = @Column(name = Message_.CONTENT + "_" + UserInputText_.RAW_INPUT)),
      @AttributeOverride( name = UserInputText_.INPUT_TRANSLATION, column = @Column(name = Message_.CONTENT + "_" + UserInputText_.INPUT_TRANSLATION))
  })
  private UserInputText content;

  private OffsetDateTime sentAt;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "from_account_id", referencedColumnName = Location_.ID)
  @QueryInit("*.*")
  private Account fromAccount;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Conversation conversation;
}
