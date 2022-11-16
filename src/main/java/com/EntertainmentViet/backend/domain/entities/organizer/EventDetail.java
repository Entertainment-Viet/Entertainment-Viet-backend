package com.EntertainmentViet.backend.domain.entities.organizer;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.domain.values.Location_;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import com.querydsl.core.annotations.QueryInit;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class EventDetail {

  @Id
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private Event event;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "location_id", referencedColumnName = Location_.ID)
  @QueryInit("*.*")
  private Location occurrenceAddress;

  @NotNull
  private OffsetDateTime occurrenceStartTime;

  @NotNull
  private OffsetDateTime occurrenceEndTime;

  @Embedded
  @AttributeOverrides({
          @AttributeOverride(name = UserInputText_.INPUT_LANG,
                  column = @Column(name = EventDetail_.DESCRIPTION + "_" + UserInputText_.INPUT_LANG)),
      @AttributeOverride( name = UserInputText_.RAW_INPUT, column = @Column(name = EventDetail_.DESCRIPTION + "_" + UserInputText_.RAW_INPUT)),
      @AttributeOverride( name = UserInputText_.INPUT_TRANSLATION, column = @Column(name = EventDetail_.DESCRIPTION + "_" + UserInputText_.INPUT_TRANSLATION))
  })
  private UserInputText description;

  @Type(type = "list-array")
  @Column(
      name = "legal_paper",
      columnDefinition = "text[]"
  )
  private List<String> legalPaper;

  public EventDetail updateInfo(EventDetail newData) {
    if (newData.getOccurrenceAddress() != null) {
      setOccurrenceAddress(newData.getOccurrenceAddress());
    }
    if (newData.getOccurrenceStartTime() != null) {
      setOccurrenceStartTime(newData.getOccurrenceStartTime());
    }
    if (newData.getOccurrenceEndTime() != null) {
      setOccurrenceEndTime(newData.getOccurrenceEndTime());
    }
    if (newData.getDescription() != null) {
      setDescription(newData.getDescription());
    }
    if (newData.getLegalPaper() != null) {
      if (getLegalPaper() == null) {
        setLegalPaper(new ArrayList<>());
      }
      getLegalPaper().addAll(newData.getLegalPaper());
    }
    return this;
  }
}
