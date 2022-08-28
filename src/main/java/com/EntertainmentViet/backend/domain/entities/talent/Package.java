package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.Booking_;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail_;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Package extends Identifiable {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank
  private String name;

  @NotNull
  private Boolean isActive;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Talent talent;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "job_detail_id", referencedColumnName = JobDetail_.ID)
  @NotNull
  private JobDetail jobDetail;

  @OneToMany
  @JoinTable(
      name = "package_order",
      joinColumns = @JoinColumn(name = "package_id", referencedColumnName = Package_.ID),
      inverseJoinColumns = @JoinColumn(name = "order_id", referencedColumnName = Booking_.ID)
  )
  private Set<Booking> orders;

  public Booking orderPackage(Organizer organizer) {
    Booking booking = new Booking();
    booking.setJobDetail(getJobDetail().clone());
    booking.setTalent(talent);
    booking.setOrganizer(organizer);
    booking.setStatus(BookingStatus.ORGANIZER_PENDING);
    return booking;
  }

  public void acceptOrder(UUID orderUid) {
    orders.stream()
        .filter(orders -> orders.getUid() == orderUid)
        .findFirst()
        .ifPresent(orders -> orders.setStatus(BookingStatus.CONFIRMED));
  }

  public void rejectOrder(UUID orderUid) {
    orders.stream()
        .filter(orders -> orders.getUid() == orderUid)
        .findFirst()
        .ifPresent(orders -> orders.setStatus(BookingStatus.CANCELLED));
  }
}
