package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.businessLogic.CronExpressionLogic;
import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.Booking_;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail_;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PackageType;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.values.RepeatPattern;
import com.EntertainmentViet.backend.exception.rest.EntityNotFoundException;
import com.EntertainmentViet.backend.exception.rest.InconsistentEntityStateException;
import com.querydsl.core.annotations.QueryInit;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Slf4j
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
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
  @QueryInit("*.*")
  private Talent talent;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  @JoinColumn(name = "job_detail_id", referencedColumnName = JobDetail_.ID)
  @NotNull
  @QueryInit("*.*")
  private JobDetail jobDetail;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "package_order",
      joinColumns = @JoinColumn(name = "package_id", referencedColumnName = Package_.ID),
      inverseJoinColumns = @JoinColumn(name = "order_id", referencedColumnName = Booking_.ID)
  )
  private Set<Booking> orders;

  @NotNull
  private Boolean archived;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "package_type")
  @Type( type = "pgsql_enum" )
  private PackageType packageType;

  @Embedded
  @QueryInit("*.*")
  private RepeatPattern repeatPattern;

  public void addOrder(Booking order) {
    orders.add(order);
    order.setTalentPackage(this);
  }

  public void removeOrder(Booking order) {
    orders.remove(order);
    order.setTalentPackage(null);
  }

  public void acceptOrder(UUID orderUid) {
    AtomicBoolean isAcceptSuccessfully = new AtomicBoolean(false);
    orders.stream()
        .filter(orders -> orders.getUid().equals(orderUid))
        .filter(orders -> orders.getStatus().equals(BookingStatus.TALENT_PENDING))
        .findAny()
        .ifPresentOrElse(
            order -> {
              var currentPrice = order.getJobDetail().getPrice();
              currentPrice.setMin(currentPrice.getMax());
              order.setStatus(BookingStatus.CONFIRMED);
              order.setConfirmedAt(OffsetDateTime.now());
              isAcceptSuccessfully.set(true);
            },
            () -> {throw new EntityNotFoundException("PackageOrder", orderUid);}
        );

    if (isAcceptSuccessfully.get()) {
      setIsActive(false);
      orders.stream()
          .filter(orders -> !orders.getUid().equals(orderUid))
          .forEach(order -> order.setStatus(BookingStatus.CANCELLED));
    }
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

  public List<Booking> generateOrder(Organizer organizer, JobDetail jobDetail, PaymentType paymentType) {
    var booking = Booking.builder()
        .jobDetail(jobDetail != null ? jobDetail : getJobDetail().clone())
        .talent(talent)
        .organizer(organizer)
        .status(BookingStatus.TALENT_PENDING)
        .createdAt(OffsetDateTime.now())
        .paymentType(paymentType)
        .isPaid(false)
        .isReview(false)
        .build();
    if (packageType == PackageType.ONCE) {
      return List.of(booking);
    } else if (packageType == PackageType.RECURRING) {
      return CronExpressionLogic.generateBookingList(booking, repeatPattern);
    } else {
      throw new InconsistentEntityStateException("Undefine package type");
    }
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
    if (newData.getRepeatPattern() != null) {
      setRepeatPattern(newData.getRepeatPattern());
      setPackageType(PackageType.RECURRING);
    } else {
      setRepeatPattern(null);
      setPackageType(PackageType.ONCE);
    }
    return this;
  }
}
