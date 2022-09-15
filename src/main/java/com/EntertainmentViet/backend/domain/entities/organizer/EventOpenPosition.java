package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.Booking_;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
}
