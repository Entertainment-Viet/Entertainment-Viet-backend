package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.Booking_;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail_;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Slf4j
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

  public Booking orderBy(Organizer organizer, PaymentType paymentType) {
    Booking booking = Booking.builder()
        .jobDetail(getJobDetail().clone())
        .talent(talent)
        .organizer(organizer)
        .status(BookingStatus.TALENT_PENDING)
        .createdAt(OffsetDateTime.now())
        .paymentType(paymentType)
        .isPaid(false)
        .build();

    orders.add(booking);
    return booking;
  }

  public void acceptOrder(UUID orderUid) {
    orders.stream()
        .filter(orders -> orders.getUid().equals(orderUid))
        .filter(orders -> orders.getStatus().equals(BookingStatus.TALENT_PENDING))
        .filter(Booking::checkIfFixedPrice)
        .findAny()
        .ifPresentOrElse(
            order -> order.setStatus(BookingStatus.CONFIRMED),
            () -> {throw new EntityNotFoundException("PackageOrder", orderUid);}
        );
  }

  public void rejectOrder(UUID orderUid) {
    orders.stream()
        .filter(orders -> orders.getUid().equals(orderUid))
        .filter(orders -> orders.getStatus().equals(BookingStatus.TALENT_PENDING))
        .findAny()
        .ifPresentOrElse(
            order -> order.setStatus(BookingStatus.CANCELLED),
            () -> {throw new EntityNotFoundException("PackageOrder", orderUid);}
        );
  }

  public Package updateInfo(Package newData) {
    if (newData.getName() != null) {
      setName(newData.getName());
    }
    if (newData.getIsActive() != null) {
      setIsActive(newData.getIsActive());
    }
    if (newData.getJobDetail() != null) {
      jobDetail.updateInfo(newData.getJobDetail());
    }
    return this;
  }
}
