package com.EntertainmentViet.backend.domain.entities.organizer;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
public class EventDetail {

  @Id
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private Event event;

  @NotNull
  private String occurrenceAddress;

  @NotNull
  private OffsetDateTime occurrenceTime;

  public EventDetail updateInfo(EventDetail newData) {
    if (newData.getOccurrenceAddress() != null) {
      setOccurrenceAddress(newData.getOccurrenceAddress());
    }
    if (newData.getOccurrenceTime() != null) {
      setOccurrenceTime(newData.getOccurrenceTime());
    }

    return this;
  }
}
