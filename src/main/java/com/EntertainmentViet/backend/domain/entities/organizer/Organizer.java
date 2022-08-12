package com.EntertainmentViet.backend.domain.entities.organizer;

import com.EntertainmentViet.backend.domain.entities.Shoppable;
import com.EntertainmentViet.backend.domain.entities.User;
import com.EntertainmentViet.backend.domain.entities.admin.OrganizerFeedback;
import com.EntertainmentViet.backend.domain.entities.admin.OrganizerFeedback_;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.domain.values.Price;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.MetaValue;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
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

  // Need to change this when upgrade to hibernate 6: https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#mapping-column-any
  @ManyToAny(metaColumn = @Column(name = "cart_item"))
  @AnyMetaDef(
      idType = "integer", metaType = "string",
      metaValues = {
        @MetaValue(value = "Package", targetEntity = Package.class)
      })
  @JoinTable(
      name = "shopping_cart",
      joinColumns = @JoinColumn(name = Organizer_.ID),
      inverseJoinColumns = @JoinColumn(name = "shoppable_id")
  )
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  private List<Shoppable> shoppables;

  public void addJobOffer(JobOffer jobOffer) {
    jobOffers.add(jobOffer);
    jobOffer.setOrganizer(this);
  }

  public void removeJobOffer(JobOffer jobOffer){
    jobOffers.remove(jobOffer);
    jobOffer.setOrganizer(null);
  }

  public void addEvent(Event event) {
    events.add(event);
    event.setOrganizer(this);
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

  public void addFeedback(OrganizerFeedback feedback) {
    feedbacks.add(feedback);
    feedback.setOrganizer(this);
  }

  public void removeFeedback(OrganizerFeedback feedback) {
    feedbacks.remove(feedback);
    feedback.setOrganizer(null);
  }

  public void review(Talent talent, Review review) {

  }

  public void addPackageToCart(Package talentPackage) {

  }

  public void finishCartShopping() {

  }

  public void pay(Price price) {

  }

}
