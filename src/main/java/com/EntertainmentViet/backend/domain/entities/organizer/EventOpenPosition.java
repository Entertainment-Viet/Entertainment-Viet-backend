package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.Booking_;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class EventOpenPosition extends Identifiable {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Event event;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "job_offer_id", referencedColumnName = JobOffer_.ID)
  @NotNull
  private JobOffer jobOffer;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "open_position_applicant",
      joinColumns = @JoinColumn( name = "open_position_id", referencedColumnName = EventOpenPosition_.ID),
      inverseJoinColumns = @JoinColumn( name = "applicant_id", referencedColumnName = Booking_.ID)
  )
  private Set<Booking> applicants;

  public void addApplicant(Booking booking) {
    applicants.add(booking);
  }

  public EventOpenPosition updateInfo(EventOpenPosition newData) {
    if (newData.getJobOffer() != null) {
      jobOffer.updateInfo(newData.getJobOffer());
    }
    return this;
  }

  public void acceptApplicant(UUID applicantUid) {
    applicants.stream()
        .filter(applicant -> applicant.getUid().equals(applicantUid))
        .filter(applicant -> applicant.getStatus().equals(BookingStatus.ORGANIZER_PENDING))
        .filter(Booking::checkIfFixedPrice)
        .findAny()
        .ifPresentOrElse(
            applicant -> applicant.setStatus(BookingStatus.CONFIRMED),
            () -> {
              throw new EntityNotFoundException("PositionApplicant", applicantUid);
            }
        );
  }

  public void rejectApplicant(UUID applicantUid) {
    applicants.stream()
        .filter(applicant -> applicant.getUid().equals(applicantUid))
        .filter(applicant -> applicant.getStatus().equals(BookingStatus.ORGANIZER_PENDING))
        .findAny()
        .ifPresentOrElse(
            applicant -> applicant.setStatus(BookingStatus.CANCELLED),
            () -> {
              throw new EntityNotFoundException("PositionApplicant", applicantUid);
            }
        );
  }
}
