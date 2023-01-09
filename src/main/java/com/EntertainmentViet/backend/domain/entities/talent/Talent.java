package com.EntertainmentViet.backend.domain.entities.talent;

import com.EntertainmentViet.backend.domain.businessLogic.ScoreSystemLogic;
import com.EntertainmentViet.backend.domain.entities.Users;
import com.EntertainmentViet.backend.domain.entities.advertisement.Advertisable;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.booking.JobDetail;
import com.EntertainmentViet.backend.domain.entities.organizer.EventOpenPosition;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.domain.standardTypes.UserState;
import com.EntertainmentViet.backend.domain.values.Category;
import com.EntertainmentViet.backend.domain.values.Category_;
import com.EntertainmentViet.backend.domain.values.Price;
import com.EntertainmentViet.backend.exception.EntityNotFoundException;
import com.EntertainmentViet.backend.features.common.utils.SecurityUtils;
import com.EntertainmentViet.backend.features.security.roles.PaymentRole;
import com.querydsl.core.annotations.QueryInit;
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
import javax.validation.constraints.NotNull;
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
public class Talent extends Users implements Advertisable {

  @OneToMany(mappedBy = Review_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews;

  @OneToMany(mappedBy = Review_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  @QueryInit("*.*")
  private List<Booking> bookings;

  @OneToMany(mappedBy = Package_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  @QueryInit("*.*")
  private Set<Package> packages;

  @OneToMany(mappedBy = PriorityScore_.TALENT, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PriorityScore> priorityScores;

  // priority score for display in browsing page
  private Double finalScore;

  @OneToOne(mappedBy = TalentDetail_.TALENT, cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
  @PrimaryKeyJoinColumn
  private TalentDetail talentDetail;

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
  @NotNull
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
              // talent only allow to update price.min
              newBookingInfo.getJobDetail().getPrice().setMax(null);
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
        .findAny()
        .ifPresentOrElse(
            booking -> {
              var currentPrice = booking.getJobDetail().getPrice();
              currentPrice.setMin(currentPrice.getMax());
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

  public void addPackage(Package aPackage) {
    packages.add(aPackage);
    aPackage.setTalent(this);
  }

  public void removePackage(Package aPackage) {
    packages.remove(aPackage);
    aPackage.setTalent(null);
  }

  public void addScore(PriorityScore score) {
    priorityScores.add(score);
    score.setTalent(this);
  }

  public void removeScore(PriorityScore score) {
    priorityScores.remove(score);
    score.setTalent(null);
  }

  public Booking applyToEventPosition(EventOpenPosition position, Double price) {
    JobDetail jobDetail = position.getJobOffer().getJobDetail().clone();
    jobDetail.getPrice().setMin(price);

    Booking newApplication = Booking.builder()
        .talent(this)
        .organizer(position.getEvent().getOrganizer())
        .jobDetail(jobDetail)
        .status(BookingStatus.ORGANIZER_PENDING)
        .createdAt(OffsetDateTime.now())
        .isPaid(false)
        .isReview(false)
        .paymentType(position.getPaymentType())
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
    return priorityScores.stream().map(PriorityScore::calculateScore).mapToDouble(Double::doubleValue).sum();
  }

  public void updateScore(List<PriorityScore> newScores, boolean isAdmin) {
    if (priorityScores == null) {
      setPriorityScores(new ArrayList<>());
    }

    ScoreSystemLogic.updateScoreSystemOfTalent(newScores, this, isAdmin);
    setFinalScore(obtainScore());
  }

  public void withdrawCash() {
    if (SecurityUtils.hasRole(PaymentRole.RECEIVE_TALENT_CASH.name())) {
      bookings.stream()
          .filter(Booking::getIsPaid)
          .filter(booking -> booking.getStatus() == BookingStatus.FINISHED)
          .forEach(booking -> booking.setStatus(BookingStatus.ARCHIVED));
    }
  }

  public Double obtainWithdrawableCash() {
    return bookings.stream()
        .filter(Booking::getIsPaid)
        .filter(booking -> booking.getStatus() != BookingStatus.ARCHIVED)
        .map(Booking::getJobDetail)
        .map(JobDetail::getPrice)
        .mapToDouble(Price::getMin)
        .sum();
  }

  public Double computeAvgReviewRate() {
    var totalReviewScore = 0.0;
    for (int i = 0; i < reviewSum.size(); i++) {
      totalReviewScore = totalReviewScore + (reviewSum.get(i) * (i+1));
    }

    return totalReviewScore / computeTotalReviewCount();
  }

  public Integer computeTotalReviewCount() {
    return reviewSum.stream().mapToInt(Integer::intValue).sum();
  }

  public Talent updateInfo(Talent newData) {
    if (newData.getTalentDetail() != null) {
      getTalentDetail().updateBasicInfo(newData.getTalentDetail());
    }
    if (newData.getDisplayName() != null) {
      setDisplayName(newData.getDisplayName());
    }
    if (newData.getOfferCategories() != null) {
      setOfferCategories(newData.getOfferCategories());
    }
    return this;
  }

  public Talent updateInfoByAdmin(Talent newData) {
    updateInfo(newData);
    if (newData.getPriorityScores() != null) {
      updateScore(newData.getPriorityScores(), true);
    }
    return this;
  }

  public Talent requestKycInfoChange(Talent newData) {
    getTalentDetail().updateKycInfo(newData.getTalentDetail());
    if (newData.getPriorityScores() != null) {
      updateScore(newData.getPriorityScores(), false);
    }
    if (newData.getUserType() != null) {
      setUserType(newData.getUserType());
    }
    // If the user already verified, change the state back to pending waiting for admin approval
    if (!getUserState().equals(UserState.GUEST)) {
      setUserState(UserState.PENDING);
    }
    return this;
  }

  @Override
  public boolean verifyAccount() {
    getPriorityScores().forEach(priorityScore -> priorityScore.setApproved(true));
    return super.verifyAccount();
  }

  @Override
  protected boolean checkIfUserVerifiable() {
    if (getUserType() == null) {
      log.warn(String.format("The talent with uid '%s' do not have userType yet", getUid()));
      return false;
    }
    if (!talentDetail.isAllKycFilled()) {
      log.warn(String.format("The talent with uid '%s' have not filled all kyc information yet", getUid()));
      return false;
    }

    return true;
  }

  @Override
  public void archive() {
    super.archive();

    getPackages().forEach(pk -> pk.setArchived(Boolean.TRUE));
  }

  private void increaseReviewSum(Review review) {
    var reviewPoint = review.getScore();
    var curPointSum = getReviewSum().get(reviewPoint-1);
    getReviewSum().set(reviewPoint-1, curPointSum+1);
  }
}
