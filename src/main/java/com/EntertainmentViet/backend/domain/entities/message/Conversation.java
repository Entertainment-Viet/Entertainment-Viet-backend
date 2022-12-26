package com.EntertainmentViet.backend.domain.entities.message;

import com.EntertainmentViet.backend.domain.entities.Account;
import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.querydsl.core.annotations.QueryInit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Conversation extends Identifiable {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private Boolean archived;

  @ManyToMany(cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
  })
  @JoinTable(name = "conversation_participant",
      joinColumns = @JoinColumn(name = "conversation_id"),
      inverseJoinColumns = @JoinColumn(name = "account_id")
  )
  @QueryInit("*.*")
  private Set<Account> participant;

  @OneToMany(mappedBy = Message_.CONVERSATION, cascade = CascadeType.ALL, orphanRemoval = true)
  @QueryInit("*.*")
  private Set<Message> messages;
}
