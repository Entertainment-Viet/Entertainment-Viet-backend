package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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
    return talentPackage.getIsActive();
  }

  public Booking charge(PaymentType paymentType) {
    return talentPackage.orderPackage(organizer, paymentType);
  }

  public OrganizerShoppingCart updateInfo(OrganizerShoppingCart newData) {
    if (newData.getPrice() != null) {
      setPrice(newData.getPrice());
    }
    return this;
  }
}
