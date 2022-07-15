package com.EntertainmentViet.backend.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Organizer implements User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

//  @NotNull
  private String address;

//  @NotNull
  private boolean temporary;
}
