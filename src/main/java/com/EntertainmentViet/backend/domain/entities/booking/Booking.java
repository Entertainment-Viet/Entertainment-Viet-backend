package com.EntertainmentViet.backend.domain.entities.booking;

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.entities.Auditable;
import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@EntityListeners({AuditableListener.class})
public class Booking extends Identifiable implements Auditable {

  @Id
  @GeneratedValue
  private Long id;

  private Instant createdAt;

  private boolean isPaid;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "booking_status")
  @Type( type = "pgsql_enum" )
  @NotNull
  private BookingStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Organizer organizer;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Talent talent;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "job_detail_id", referencedColumnName = JobDetail_.ID)
  @NotNull
  private JobDetail jobDetail;

  public void updateInfo(Booking newData) {

  }
}
