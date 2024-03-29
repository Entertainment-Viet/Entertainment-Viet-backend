package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.*;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Review;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.exception.rest.InvalidInputException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@UtilityClass
@Slf4j
public class EntityValidationUtils {

  // Organizer //
  public boolean isOrganizerWithUid(Organizer organizer, UUID uid) {
    if (organizer == null) {
      throw new InvalidInputException(String.format("Can not find organizer with id '%s'", uid));
    }
    return true;
  }

  // Talent //
  public boolean isTalentWithUid(Talent talent, UUID uid) {
    if (talent == null) {
      throw new InvalidInputException(String.format("Can not find talent with id '%s'", uid));
    }
    return true;
  }

  // Booking //
  public boolean isBookingWithUid(Booking booking, UUID uid) {
    if (booking == null) {
      throw new InvalidInputException(String.format("Can not find booking with id '%s' ", uid));
    }
    return true;
  }

  public boolean isBookingBelongToOrganizerWithUid(Booking booking, UUID organizerUid) {
    if (booking.getOrganizer() == null) {
      throw new InvalidInputException(String.format("Can not find organizer owning the booking with id '%s'", booking.getUid()));
    }

    Organizer organizer = booking.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      throw new InvalidInputException(String.format("Can not find any booking with id '%s' belong to organizer with id '%s'", booking.getUid(), organizerUid));
    }
    return true;
  }

  public boolean isBookingBelongToTalentWithUid(Booking booking, UUID talentUid) {
    if (booking.getTalent() == null) {
      throw new InvalidInputException(String.format("Can not find talent owning the booking with id '%s'", booking.getUid()));
    }

    Talent talent = booking.getTalent();
    if (!talent.getUid().equals(talentUid)) {
      throw new InvalidInputException(String.format("Can not find any booking with id '%s' belong to talent with id '%s'", booking.getUid(), talentUid));
    }
    return true;
  }

  public boolean isBookingValid(Booking booking, UUID organizerUid, UUID talentUid) {
    if (booking.getOrganizer() == null) {
      throw new InvalidInputException(String.format("Can not find organizer with id '%s' to creating a booking", organizerUid));
    }

    if (booking.getTalent() == null) {
      throw new InvalidInputException(String.format("Can not find talent with id '%s' to creating a booking", talentUid));
    }

    if (booking.getJobDetail() == null) {
      throw new InvalidInputException("Provided jobDetail for creating booking is invalid");
    }
    return true;
  }

  // Job Offer //
  public boolean isJobOfferWithUidExist(JobOffer jobOffer, UUID uid) {
    if (jobOffer == null) {
      throw new InvalidInputException(String.format("Can not find jobOffer with id '%s'", uid));
    }
    return true;
  }

  public boolean isJobOfferBelongToOrganizerWithUid(JobOffer jobOffer, UUID organizerUid) {
    Organizer organizer = jobOffer.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      throw new InvalidInputException(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", jobOffer.getUid(), organizerUid));
    }
    return true;
  }

  public boolean isJobOfferValid(JobOffer jobOffer) {
    if (jobOffer.getOrganizer() == null) {
      throw new InvalidInputException(String.format("Can not find organizer owning the jobOffer with id '%s'", jobOffer.getUid()));
    }

    if (jobOffer.getJobDetail() == null || jobOffer.getJobDetail().getCategory() == null) {
      throw new InvalidInputException(String.format("Can not populate jobDetail information for jobOffer with id '%s'", jobOffer.getUid()));
    }
    return true;
  }

  // Shopping cart item //
  public boolean isCartItemWithUidExist(OrganizerShoppingCart cartItem, UUID uid) {
    if (cartItem == null) {
      throw new InvalidInputException(String.format("Can not find cart item with id '%s'", uid));
    }
    return true;
  }

  public boolean isCartItemBelongToOrganizerWithUid(OrganizerShoppingCart cartItem, UUID organizerUid) {
    Organizer organizer = cartItem.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      throw new InvalidInputException(String.format("Can not find any shopping cart item with id '%s' belong to organizer with id '%s'", cartItem.getUid(), organizerUid));
    }
    return true;
  }

  // Event //
  public boolean isOpenPositionWithUidExist(EventOpenPosition eventOpenPosition, UUID uid) {
    if (eventOpenPosition == null) {
      throw new InvalidInputException(String.format("Can not find event open position with id '%s'", uid));
    }
    return true;
  }

  public boolean isOpenPositionBelongToEventWithUid(EventOpenPosition eventOpenPosition, UUID eventUid) {
    Event event = eventOpenPosition.getEvent();
    if (!event.getUid().equals(eventUid)) {
      throw new InvalidInputException(String.format("Can not find any event open position with id '%s' belong to event with id '%s'", eventOpenPosition.getUid(), eventUid));
    }
    return true;
  }

  public boolean isEventWithUidExist(Event event, UUID uid) {
    if (event == null) {
      throw new InvalidInputException(String.format("Can not find event with id '%s'", uid));
    }
    return true;
  }

  public boolean isEventBelongToOrganizerWithUid(Event event, UUID organizerUid) {
    Organizer organizer = event.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      throw new InvalidInputException(String.format("Can not find any event with id '%s' belong to organizer with id '%s'", event.getUid(), organizerUid));
    }
    return true;
  }

  // Package //
  public boolean isPackageWithUidExist(Package packageTalent, UUID uid) {
    if (packageTalent == null) {
      throw new InvalidInputException(String.format("Can not find package with id '%s' ", uid));
    }
    return true;
  }

  public boolean isPackageBelongToTalentWithUid(Package packageTalent, UUID talentId) {
    if (packageTalent.getTalent() == null) {
      throw new InvalidInputException(String.format("Can not find talent owning the package with id '%s'", packageTalent.getUid()));
    }

    Talent talent = packageTalent.getTalent();
    if (!talent.getUid().equals(talentId)) {
      throw new InvalidInputException(String.format("There is no talent with id '%s' have the package with id '%s'", talentId, packageTalent.getUid()));
    }
    return true;
  }

  // Review //
  public boolean isReviewWithUidExist(Review review, UUID uid) {
    if (review == null) {
      throw new InvalidInputException(String.format("Can not find review with id '%s' ", uid));
    }
    return true;
  }

  public boolean isReviewBelongToTalentWithUid(Review review, UUID talentId) {
    if (review.getTalent() == null) {
      throw new InvalidInputException(String.format("Can not find talent have the review with id '%s'", review.getUid()));
    }

    Talent talent = review.getTalent();
    if (!talent.getUid().equals(talentId)) {
      throw new InvalidInputException(String.format("There is no talent with id '%s' have the review with id '%s'", talentId, review.getUid()));
    }
    return true;
  }
}
