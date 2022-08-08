package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.entities.User;
import com.EntertainmentViet.backend.domain.entities.admin.TalentFeedback;
import com.EntertainmentViet.backend.domain.entities.admin.TalentFeedback_;
import com.EntertainmentViet.backend.domain.entities.advertisement.Advertisable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Talent extends User implements Advertisable {

  @OneToMany(mappedBy = Review_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews;

  @OneToMany(mappedBy = Review_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Booking> bookings;

  @OneToMany(mappedBy = TalentFeedback_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TalentFeedback> feedbacks;

  public void addReview(Review review) {
    reviews.add(review);
    review.setTalent(this);
  }

  public void removeReview(Review review) {
    reviews.remove(review);
    review.setTalent(null);
  }

  public void addBooking(Booking booking) {
    bookings.add(booking);
    booking.setTalent(this);
  }

  public void removeBooking(Booking booking) {
    bookings.remove(booking);
    booking.setTalent(null);
  }

  public void addFeedback(TalentFeedback feedback) {
    feedbacks.add(feedback);
    feedback.setTalent(this);
  }

  public void removeFeedback(TalentFeedback feedback) {
    feedbacks.remove(feedback);
    feedback.setTalent(null);
  }
}
