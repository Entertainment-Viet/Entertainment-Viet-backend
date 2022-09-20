package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.advertisement.Advertisable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Slf4j
public class Event extends Identifiable implements Advertisable {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @NotNull
  private Boolean isActive;

  @NotNull
  private String occurrenceAddress;

  @NotNull
  private OffsetDateTime occurrenceTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Organizer organizer;

  @OneToMany(mappedBy = EventOpenPosition_.EVENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EventOpenPosition> openPositions;

  public void addOpenPosition(EventOpenPosition eventOpenPosition) {
    openPositions.add(eventOpenPosition);
    eventOpenPosition.setEvent(this);
  }

  public void removeOpenPosition(EventOpenPosition eventOpenPosition) {
    openPositions.remove(eventOpenPosition);
    eventOpenPosition.setEvent(null);
  }

  public void updateOpenPosition(UUID positionUid, EventOpenPosition eventOpenPosition) {
    openPositions.stream()
        .filter(position -> position.getUid().equals(positionUid))
        .findAny()
        .ifPresentOrElse(
            position -> position.updateInfo(eventOpenPosition),
            () -> log.warn(String.format("Can not update information of openPosition with uid '%s'", positionUid))
        );
  }
}
