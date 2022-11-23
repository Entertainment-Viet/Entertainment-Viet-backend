package com.EntertainmentViet.backend.domain.entities.organizer;

import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.advertisement.Advertisable;
import com.querydsl.core.annotations.QueryInit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  @QueryInit("*.*")
  private Organizer organizer;

  @OneToOne(mappedBy = EventDetail_.EVENT, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  @PrimaryKeyJoinColumn
  @QueryInit("*.*")
  private EventDetail eventDetail;

  @OneToMany(mappedBy = EventOpenPosition_.EVENT, cascade = CascadeType.ALL, orphanRemoval = true)
  @QueryInit("*.*")
  private List<EventOpenPosition> openPositions;

  @NotNull
  private Boolean archived;

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

  public Event updateInfo(Event newData) {
    if (newData.getName() != null) {
      setName(newData.getName());
    }
    if (newData.getIsActive() != null) {
      setIsActive(newData.getIsActive());
    }
    if (newData.getEventDetail() != null) {
      getEventDetail().updateInfo(newData.getEventDetail());
    }

    return this;
  }
}
