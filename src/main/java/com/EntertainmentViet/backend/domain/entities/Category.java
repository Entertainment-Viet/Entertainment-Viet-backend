package com.EntertainmentViet.backend.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Category {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private Long parentId;
}
