package com.EntertainmentViet.backend.domain.entities.booking;

import com.EntertainmentViet.backend.domain.standardTypes.WorkType;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.Price;
import com.EntertainmentViet.backend.domain.values.UserInputText;
import com.EntertainmentViet.backend.domain.values.UserInputText_;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;
import javax.persistence.Embedded;
import javax.persistence.AttributeOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class JobDetail implements Serializable {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "work_type")
  @Type( type = "pgsql_enum" )
  @NotNull
  private WorkType workType;

  @Embedded
  @NotNull
  private Price price;

  @NotNull
  private OffsetDateTime performanceStartTime;

  @NotNull
  private OffsetDateTime performanceEndTime;

  @NotNull
  private Integer performanceCount;

  private String location;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride( name = UserInputText_.INPUT_LANG, column = @Column(name = JobDetail_.NOTE + "_" + UserInputText_.INPUT_LANG)),
      @AttributeOverride( name = UserInputText_.RAW_INPUT, column = @Column(name = JobDetail_.NOTE + "_" + UserInputText_.RAW_INPUT)),
      @AttributeOverride( name = UserInputText_.INPUT_TRANSLATION, column = @Column(name = JobDetail_.NOTE + "_" + UserInputText_.INPUT_TRANSLATION))
  })
  private UserInputText note;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode extensions;

  public JobDetail clone() {
    return JobDetail.builder()
        .category(getCategory())
        .workType(getWorkType())
        .price(getPrice())
        .performanceStartTime(getPerformanceStartTime())
        .performanceEndTime(getPerformanceEndTime())
        .performanceCount(getPerformanceCount())
        .location(getLocation())
        .note(getNote())
        .build();
  }

  public JobDetail updateInfo(JobDetail newData) {
    if (newData.getCategory() != null) {
      setCategory(newData.getCategory());
    }
    if (newData.getPrice() != null) {
      price.updateInfo(newData.getPrice());
    }
    if (newData.getPerformanceEndTime() != null) {
      setPerformanceEndTime(newData.getPerformanceEndTime());
    }
    if (newData.getPerformanceStartTime() != null) {
      setPerformanceStartTime(newData.getPerformanceStartTime());
    }
    if (newData.getPerformanceCount() != null) {
      setPerformanceCount(newData.getPerformanceCount());
    }
    if (newData.getLocation() != null) {
      setLocation(newData.getLocation());
    }
    if (newData.getNote() != null) {
      setNote(newData.getNote());
    }
    return this;
  }
}
