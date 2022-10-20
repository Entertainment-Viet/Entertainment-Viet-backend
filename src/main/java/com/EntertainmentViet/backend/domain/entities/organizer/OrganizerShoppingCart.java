package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.values.Price;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.OffsetDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrganizerShoppingCart extends Identifiable {

  @EmbeddedId
  private ShoppingCartKey id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId(ShoppingCartKey_.ORGANIZER_ID)
  private Organizer organizer;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId(ShoppingCartKey_.PACKAGE_ID)
  @JoinColumn(name = "package_id")
  private Package talentPackage;

  private Double price;

  public OrganizerShoppingCart(Organizer organizer, Package talentPackage, Double price) {
    this.organizer = organizer;
    this.talentPackage = talentPackage;
    this.price = price;
    this.id = new ShoppingCartKey(organizer.getId(), talentPackage.getId());
  }

  public Boolean checkValidCartItem() {
    if (!talentPackage.getIsActive()) {
      return false;
    }

    return true;
  }

  public Booking generateBooking(PaymentType paymentType) {
    JobDetail jobDetail = talentPackage.getJobDetail().clone();
    jobDetail.getPrice().setMax(price);
    jobDetail.setLocation(organizer.getOrganizerDetail().getAddress());

    return talentPackage.generateOrder(organizer, jobDetail, paymentType);
  }

  public OrganizerShoppingCart updateInfo(OrganizerShoppingCart newData) {
    if (newData.getPrice() != null) {
      setPrice(newData.getPrice());
    }
    return this;
  }
}
