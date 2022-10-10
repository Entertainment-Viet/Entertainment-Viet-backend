package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.User;
import com.EntertainmentViet.backend.domain.entities.admin.OrganizerFeedback;
import com.EntertainmentViet.backend.domain.entities.admin.OrganizerFeedback_;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.security.roles.PaymentRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Slf4j
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Organizer extends User {

  @OneToMany(mappedBy = JobOffer_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JobOffer> jobOffers;

  @OneToMany(mappedBy = Event_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Event> events;

  @OneToMany(mappedBy = Event_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Booking> bookings;

  @OneToMany(mappedBy = OrganizerFeedback_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrganizerFeedback> feedbacks;

  @OneToMany(mappedBy = OrganizerShoppingCart_.ORGANIZER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrganizerShoppingCart> shoppingCart;

  public void addJobOffer(JobOffer jobOffer) {
    jobOffers.add(jobOffer);
    jobOffer.setOrganizer(this);
  }

  public void removeJobOffer(JobOffer jobOffer) {
    jobOffers.remove(jobOffer);
    jobOffer.setOrganizer(null);
  }

  public void addEvent(Event event) {
    events.add(event);
    event.setOrganizer(this);
  }

  public void disableEvent(UUID eventUid) {
    events.stream()
        .filter(event -> event.getUid().equals(eventUid))
        .findFirst()
        .ifPresent( event -> event.setIsActive(false));
  }

  public void removeEvent(Event event) {
    events.remove(event);
    event.setOrganizer(null);
  }

  public void addBooking(Booking booking) {
    bookings.add(booking);
    booking.setOrganizer(this);
  }

  public void removeBooking(Booking booking) {
    bookings.remove(booking);
    booking.setOrganizer(null);
  }

  public void updateBookingInfo(UUID bookingUid, Booking newBookingInfo) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .findAny()
        .ifPresentOrElse(
            booking -> {
              booking.updateInfo(newBookingInfo);
              booking.setStatus(BookingStatus.TALENT_PENDING);
            },
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void acceptBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .filter(booking -> booking.getStatus().equals(BookingStatus.ORGANIZER_PENDING))
        .filter(Booking::checkIfFixedPrice)
        .findAny()
        .ifPresentOrElse(
            booking -> {
              booking.setStatus(BookingStatus.CONFIRMED);
              booking.setConfirmedAt(OffsetDateTime.now());
            },
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void rejectBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .filter(booking -> booking.getStatus().equals(BookingStatus.ORGANIZER_PENDING))
        .findAny()
        .ifPresentOrElse(
            booking -> booking.setStatus(BookingStatus.CANCELLED),
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void addFeedback(OrganizerFeedback feedback) {
    feedbacks.add(feedback);
    feedback.setOrganizer(this);
  }

  public void removeFeedback(OrganizerFeedback feedback) {
    feedbacks.remove(feedback);
    feedback.setOrganizer(null);
  }

  public void addPackageToCart(Package talentPackage, Double price) {
    OrganizerShoppingCart cartItem = new OrganizerShoppingCart(this, talentPackage, price);
    shoppingCart.add(cartItem);
  }

  public void finishCartShopping(PaymentType paymentType) {
    // validate cart item
    var invalidPackage = shoppingCart.stream()
        .filter(Predicate.not(OrganizerShoppingCart::checkValidCartItem))
        .collect(Collectors.toList());
    if (invalidPackage.size() != 0) {
      log.warn("Exist invalid cart item in shopping cart");
      throw new EntityNotFoundException("Package", invalidPackage.get(0).getTalentPackage().getUid());
    }
    for (OrganizerShoppingCart cartItem : shoppingCart) {
      addBooking(cartItem.charge(paymentType));
    }

    clearCart();
  }

  public void clearCart() {
    shoppingCart.clear();
  }

  public void pay(Booking booking) {
    if (SecurityUtils.hasRole(PaymentRole.PAY_ORGANIZER_CASH.name()) && booking.checkIfFixedPrice()) {
      booking.setPaid(true);
    }
  }

  public void makeBookingFromJobOffer(JobOffer jobOffer, Talent talent) {
    Booking newBooking = jobOffer.sendOffer(talent);
    addBooking(newBooking);
    talent.addBooking(newBooking);
  }

  public Organizer updateInfo(Organizer newData) {
    if (newData.getPhoneNumber() != null) {
      setPhoneNumber(newData.getPhoneNumber());
    }
    if (newData.getEmail() != null) {
      setEmail(newData.getEmail());
    }
    if (newData.getAddress() != null) {
      setAddress(newData.getAddress());
    }
    if (newData.getBio() != null) {
      setBio(newData.getBio());
    }
    if (newData.getExtensions() != null) {
      setExtensions(newData.getExtensions());
    }
    if (newData.getDisplayName() != null) {
      setDisplayName(newData.getDisplayName());
    }
    if (newData.getJobOffers() != null) {
      setJobOffers(newData.getJobOffers());
    }
    if (newData.getBookings() != null) {
      setBookings(newData.getBookings());
    }
    if (newData.getEvents() != null) {
      setEvents(newData.getEvents());
    }
    if (newData.getFeedbacks() != null) {
      setFeedbacks(newData.getFeedbacks());
    }
    if (newData.getShoppingCart() != null) {
      setShoppingCart(newData.getShoppingCart());
    }
    return this;
  }

}
