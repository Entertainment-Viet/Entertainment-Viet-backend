package com.EntertainmentViet.backend.domain.entities.organizer;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShoppingCartKey implements Serializable {

  private Long organizerId;

  private Long packageId;
}
