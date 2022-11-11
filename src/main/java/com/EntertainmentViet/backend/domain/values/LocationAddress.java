package com.EntertainmentViet.backend.domain.values;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class LocationAddress extends Identifiable implements Serializable {

  @Id
  @GeneratedValue()
  private Long id;

  private String street;

  private String district;

  @NotNull
  private String city;

  @NotNull
  private Point coordinates;
}
