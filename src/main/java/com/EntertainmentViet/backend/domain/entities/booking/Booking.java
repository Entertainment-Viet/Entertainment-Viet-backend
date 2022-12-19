package com.EntertainmentViet.backend.domain.entities.booking;

import java.time.OffsetDateTime;
import java.util.List;
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

import com.EntertainmentViet.backend.domain.businessLogic.AuditableListener;
import com.EntertainmentViet.backend.domain.businessLogic.BookingCodeListener;
import com.EntertainmentViet.backend.domain.entities.Auditable;
import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.organizer.Organizer;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.annotations.QueryInit;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
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
@TypeDef(
    name = "pgsql_enum",
    typeClass = PostgreSQLEnumType.class
)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@EntityListeners({AuditableListener.class, BookingCodeListener.class})
public class Booking extends Identifiable implements Auditable {

  @Id
  @GeneratedValue
  private Long id;

  private OffsetDateTime createdAt;

  private OffsetDateTime confirmedAt;

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
  @QueryInit("*.*")
  private Talent talent;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
  @JoinColumn(name = "job_detail_id", referencedColumnName = JobDetail_.ID)
  @NotNull
  @QueryInit("*.*")
  private JobDetail jobDetail;

  @ManyToOne(fetch = FetchType.LAZY)
  @QueryInit("*.*")
  private Package talentPackage;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "payment_type")
  @Type( type = "pgsql_enum" )
  @NotNull
  private PaymentType paymentType;

  @NotNull
  private Boolean isReview;

  @Type(type = "list-array")
  @Column(
      name = "finish_proof",
      columnDefinition = "text[]"
  )
  private List<String> finishProof;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode extensions;

  private String bookingCode;

  public void updateInfo(Booking newData) {
    if (checkIfConfirmed()) {
      return;
    }
    if (newData.getJobDetail() != null) {
      jobDetail.updateInfo(newData.getJobDetail());
    }
    if (newData.getPaymentType() != null) {
      setPaymentType(newData.getPaymentType());
    }
    if (newData.getExtensions() != null) {
      setExtensions(newData.getExtensions());
    }
    if (newData.getFinishProof() != null) {
      setFinishProof(newData.getFinishProof());
    }
  }

  public boolean checkIfFixedPrice() {
    return getJobDetail().getPrice().checkIfFixedPrice();
  }

  public boolean checkIfConfirmed() {
    return !status.equals(BookingStatus.TALENT_PENDING) && !status.equals(BookingStatus.ORGANIZER_PENDING);
  }
}
