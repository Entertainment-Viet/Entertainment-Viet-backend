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
import com.EntertainmentViet.backend.domain.standardTypes.PaymentType;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.Category_;
import com.EntertainmentViet.backend.domain.values.Price;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.admin.dto.talent.ScoreOperandDto;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.security.roles.PaymentRole;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Slf4j
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDef(name = "list-array",typeClass = ListArrayType.class)
public class Talent extends User implements Advertisable {

  @OneToMany(mappedBy = Review_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews;

  @OneToMany(mappedBy = Review_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Booking> bookings;

  @OneToMany(mappedBy = TalentFeedback_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TalentFeedback> feedbacks;

  @OneToMany(mappedBy = Package_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Package> packages;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private Map<String, ScoreOperandDto> scoreSystem;

  private Double finalScore;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
          name = "talent_category",
          joinColumns = @JoinColumn( name = "talent_id", referencedColumnName = Talent_.ID),
          inverseJoinColumns = @JoinColumn( name = "category_id", referencedColumnName = Category_.ID)
  )
  private Set<Category> offerCategories;

  @Type(type = "list-array")
  @Column(
      name = "review_sum",
      columnDefinition = "integer[]"
  )
  private List<Integer> reviewSum;

  public void addOfferCategory(Category category) {
    offerCategories.add(category);
  }

  public void removeOfferCategory(Category category) {
    offerCategories.remove(category);
  }

  public void addReview(Review review) {
    reviews.add(review);
    review.setTalent(this);
    increaseReviewSum(review);
  }

  public void hideReview(Review review) {
    reviews.remove(review);
    review.setTalent(null);
  }

  public void addBooking(Booking booking) {
    bookings.add(booking);
    booking.setTalent(this);
  }

  public void updateBookingInfo(UUID bookingUid, Booking newBookingInfo) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .findAny()
        .ifPresentOrElse(
            booking -> {
              booking.updateInfo(newBookingInfo);
              booking.setStatus(BookingStatus.ORGANIZER_PENDING);
            },
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void acceptBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .filter(booking -> booking.getStatus().equals(BookingStatus.TALENT_PENDING))
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
        .filter(booking -> booking.getStatus().equals(BookingStatus.TALENT_PENDING))
        .findAny()
        .ifPresentOrElse(
            booking -> booking.setStatus(BookingStatus.CANCELLED),
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
  }

  public void finishBooking(UUID bookingUid) {
    bookings.stream()
        .filter(booking -> booking.getUid().equals(bookingUid))
        .filter(booking -> booking.getStatus().equals(BookingStatus.CONFIRMED) || booking.getStatus().equals(BookingStatus.ORGANIZER_FINISH))
        .findAny()
        .ifPresentOrElse(
            booking -> {
              if (booking.getStatus().equals(BookingStatus.CONFIRMED)) {
                booking.setStatus(BookingStatus.TALENT_FINISH);
              } else if (booking.getStatus().equals(BookingStatus.ORGANIZER_FINISH)) {
                booking.setStatus(BookingStatus.FINISHED);
              }
            },
            () -> {throw new EntityNotFoundException("Booking", bookingUid);}
        );
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

  public void addPackage(Package aPackage) {
    packages.add(aPackage);
    aPackage.setTalent(this);
  }

  public void removePackage(Package aPackage) {
    packages.remove(aPackage);
    aPackage.setTalent(null);
  }

  public Booking applyToEventPosition(EventOpenPosition position, PaymentType paymentType) {
    Booking newApplication = Booking.builder()
        .talent(this)
        .organizer(position.getEvent().getOrganizer())
        .jobDetail(position.getJobOffer().getJobDetail().clone())
        .status(BookingStatus.ORGANIZER_PENDING)
        .createdAt(OffsetDateTime.now())
        .isPaid(false)
        .paymentType(paymentType)
        .build();

    position.addApplicant(newApplication);
    return newApplication;
  }

  public OptionalDouble obtainAvgReviewScore() {
    return reviews.stream()
        .mapToInt(Review::getScore)
        .average();
  }

  public Double obtainScore() {
    return TalentScoreCalculator.calculateScore(getScoreSystem());
  }

  public void updateScore(Map<String, ScoreOperandDto> scoreData) {
    if (scoreSystem == null) {
      setScoreSystem(new HashMap<>());
    }

    scoreData.forEach((key, value) -> {
      if (scoreSystem.containsKey(key)) {
        var currentScoreOperand = scoreSystem.get(key);
        if (value.getName() != null) {
          currentScoreOperand.setName(value.getName());
        }
        if (value.getRate() != null) {
          currentScoreOperand.setRate(value.getRate());
        }
        if (value.getMultiply() != null) {
          currentScoreOperand.setMultiply(value.getMultiply());
        }
        if (value.getActive() != null) {
          currentScoreOperand.setActive(value.getActive());
        }
      }
      // Only add when there is a rate
      else if (value.getRate() != null) {
        scoreSystem.put(key, value);
      }
    });

    setFinalScore(obtainScore());
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

  public Double computeUnpaidSum() {
    return bookings.stream()
        .filter(booking -> booking.getStatus().equals(BookingStatus.FINISHED))
        .map(Booking::getJobDetail)
        .map(JobDetail::getPrice)
        .map(Price::getMax)
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  public Talent updateInfo(Talent newData) {
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
    if (newData.getReviews() != null) {
      setReviews(newData.getReviews());
    }
    if (newData.getBookings() != null) {
      setBookings(newData.getBookings());
    }
    if (newData.getFeedbacks() != null) {
      setFeedbacks(newData.getFeedbacks());
    }
    if (newData.getOfferCategories() != null) {
      setOfferCategories(newData.getOfferCategories());
    }
    if (newData.getScoreSystem() != null) {
      updateScore(newData.getScoreSystem());
    }
    return this;
  }

  private void increaseReviewSum(Review review) {
    var reviewPoint = review.getScore();
    var curPointSum = getReviewSum().get(reviewPoint-1);
    getReviewSum().set(reviewPoint-1, curPointSum+1);
  }
}
