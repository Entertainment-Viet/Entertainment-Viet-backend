package com.EntertainmentViet.backend.domain.values;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Geometry;
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Location extends Identifiable {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "type_id", referencedColumnName = Location_.ID)
  private LocationType typeId;

  @NotNull
  private String name;

  private Long nameCode;

  private Long zipcode;

  @JsonSerialize(using = GeometrySerializer.class)
  @JsonDeserialize(using = GeometryDeserializer.class)
  private Geometry coordinate;

  @JsonSerialize(using = GeometrySerializer.class)
  @JsonDeserialize(using = GeometryDeserializer.class)
  private Geometry boundary;

  @ManyToOne(fetch = FetchType.LAZY)
  private Location parent;
}
