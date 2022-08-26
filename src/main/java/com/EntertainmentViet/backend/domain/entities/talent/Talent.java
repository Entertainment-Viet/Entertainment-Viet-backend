package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.businessLogic.TalentScoreCalculator;
import com.EntertainmentViet.backend.domain.entities.User;
import com.EntertainmentViet.backend.domain.entities.admin.TalentFeedback;
import com.EntertainmentViet.backend.domain.entities.admin.TalentFeedback_;
import com.EntertainmentViet.backend.domain.entities.advertisement.Advertisable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.values.Price;
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
import java.time.Instant;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Slf4j
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Talent extends User implements Advertisable {

  private static final String SCORE_METRIC_PATH = "scoreMetrics";

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

  public void acceptBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid() == bookingUid)
        .filter(Booking::checkIfFixedPrice)
        .findFirst()
        .ifPresentOrElse(
            booking -> booking.setStatus(BookingStatus.CONFIRMED),
            () -> log.error("Can not accept the booking: ", bookingUid)
        );
  }

  public void rejectBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid() == bookingUid)
        .findFirst()
        .ifPresent(booking -> booking.setStatus(BookingStatus.CANCELLED));
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

  public Booking applyToEventPosition(EventOpenPosition position) {
    Booking newApplication = Booking.builder()
                                    .talent(this)
                                    .organizer(position.getEvent().getOrganizer())
                                    .jobDetail(position.getJobOffer().getJobDetail())
                                    .status(BookingStatus.ORGANIZER_PENDING)
                                    .createdAt(Instant.now())
                                    .isPaid(false)
                                    .build();

    position.addApplicant(newApplication);
    return newApplication;
  }

  public OptionalDouble obtainAvgReviewScore() {
    return reviews.stream()
        .mapToInt(Review::getScore)
        .average();
  }

  public OptionalDouble obtainScore() {
    return TalentScoreCalculator.calculateScore(getExtensions().get(SCORE_METRIC_PATH));
  }

  public void withdrawCash() {
    if (SecurityUtils.hasRole(PaymentRole.RECEIVE_TALENT_CASH.name())) {
      bookings.stream()
          .filter(Booking::isPaid)
          .filter(booking -> booking.getStatus() == BookingStatus.FINISHED)
          .forEach(booking -> booking.setStatus(BookingStatus.ARCHIVED));
    }
  }

  public Double obtainWithdrawableCash() {
    return bookings.stream()
        .filter(Booking::isPaid)
        .filter(booking -> booking.getStatus() != BookingStatus.ARCHIVED)
        .map(Booking::getJobDetail)
        .map(JobDetail::getPrice)
        .mapToDouble(Price::getMin)
        .sum();
  }
}
