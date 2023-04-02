package com.EntertainmentViet.backend.domain.entities;

import com.EntertainmentViet.backend.domain.businessLogic.IdentifiableListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners({IdentifiableListener.class})
public abstract class Identifiable implements Serializable {

  @EqualsAndHashCode.Include
  private UUID uid;
}
