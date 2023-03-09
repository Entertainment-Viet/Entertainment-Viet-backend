package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.Searchable;
import com.EntertainmentViet.backend.domain.entities.advertisement.Advertisable;
import com.querydsl.core.annotations.QueryInit;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
@Slf4j
public class Event extends Identifiable implements Advertisable, Searchable {

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

  @Type(type = "list-array")
  @Column(
      name = "hash_tag",
      columnDefinition = "text[]"
  )
  @NotNull
  private List<String> hashTag;


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
    if (newData.getHashTag() != null) {
      setHashTag(newData.getHashTag());
    }
    return this;
  }

  public void archive() {
    setArchived(true);
    getOpenPositions().forEach(position -> position.setArchived(true));
  }
}
