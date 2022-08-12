package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail_;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class JobOffer extends Identifiable {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private Boolean isActive;

  @NotNull
  private Integer quantity;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "job_detail_id", referencedColumnName = JobDetail_.ID)
  @NotNull
  private JobDetail jobDetail;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Organizer organizer;

  public Booking sendOffer(Talent talent) {
    Booking booking = new Booking();
    booking.setJobDetail(getJobDetail());
    booking.setTalent(talent);
    booking.setOrganizer(getOrganizer());
    booking.setStatus(BookingStatus.ORGANIZER_PENDING);
    return booking;
  }
}
