package com.EntertainmentViet.backend.features.common.utils;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.entities.organizer.*;
import com.EntertainmentViet.backend.domain.entities.talent.Package;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@UtilityClass
@Slf4j
public class EntityValidationUtils {

  // Booking //
  public boolean isBookingWithUid(Booking booking, UUID uid) {
    if (booking == null) {
      log.warn(String.format("Can not find booking with id '%s' ", uid));
      return false;
    }
    return true;
  }

  public boolean isBookingBelongToOrganizerWithUid(Booking booking, UUID organizerUid) {
    if (booking.getOrganizer() == null) {
      log.warn(String.format("Can not find organizer owning the booking with id '%s'", booking.getUid()));
      return false;
    }

    Organizer organizer = booking.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      log.warn(String.format("Can not find any booking with id '%s' belong to organizer with id '%s'", booking.getUid(), organizerUid));
      return false;
    }
    return true;
  }

  public boolean isBookingBelongToTalentWithUid(Booking booking, UUID talentUid) {
    if (booking.getTalent() == null) {
      log.warn(String.format("Can not find talent owning the booking with id '%s'", booking.getUid()));
      return false;
    }

    Talent talent = booking.getTalent();
    if (!talent.getUid().equals(talentUid)) {
      log.warn(String.format("Can not find any booking with id '%s' belong to talent with id '%s'", booking.getUid(), talentUid));
      return false;
    }
    return true;
  }

  // Job Offer //
  public boolean isJobOfferWithUidExist(JobOffer jobOffer, UUID uid) {
    if (jobOffer == null) {
      log.warn(String.format("Can not find jobOffer with id '%s'", uid));
      return false;
    }
    return true;
  }

  public boolean isJobOfferBelongToOrganizerWithUid(JobOffer jobOffer, UUID organizerUid) {
    Organizer organizer = jobOffer.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      log.warn(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", jobOffer.getUid(), organizerUid));
      return false;
    }
    return true;
  }

  public boolean isJobOfferValid(JobOffer jobOffer) {
    if (jobOffer.getOrganizer() == null) {
      log.warn(String.format("Can not find organizer owning the jobOffer with id '%s'", jobOffer.getUid()));
      return false;
    }

    if (jobOffer.getJobDetail() == null || jobOffer.getJobDetail().getCategory() == null) {
      log.warn(String.format("Can not populate jobDetail information for jobOffer with id '%s'", jobOffer.getUid()));
      return false;
    }
    return true;
  }

  // Shopping cart item //
  public boolean isCartItemWithUidExist(OrganizerShoppingCart cartItem, UUID uid) {
    if (cartItem == null) {
      log.warn(String.format("Can not find jobOffer with id '%s'", uid));
      return false;
    }
    return true;
  }

  public boolean isCartItemBelongToOrganizerWithUid(OrganizerShoppingCart cartItem, UUID organizerUid) {
    Organizer organizer = cartItem.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      log.warn(String.format("Can not find any shopping cart item with id '%s' belong to organizer with id '%s'", cartItem.getUid(), organizerUid));
      return false;
    }
    return true;
  }

  // Event //
  public boolean isOpenPositionWithUidExist(EventOpenPosition eventOpenPosition, UUID uid) {
    if (eventOpenPosition == null) {
      log.warn(String.format("Can not find event open position with id '%s'", uid));
      return false;
    }
    return true;
  }

  public boolean isOpenPositionBelongToEventWithUid(EventOpenPosition eventOpenPosition, UUID eventUid) {
    Event event = eventOpenPosition.getEvent();
    if (!event.getUid().equals(eventUid)) {
      log.warn(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", eventOpenPosition.getUid(), eventUid));
      return false;
    }
    return true;
  }

  public boolean isEventWithUidExist(Event event, UUID uid) {
    if (event == null) {
      log.warn(String.format("Can not find event with id '%s'", uid));
      return false;
    }
    return true;
  }

  public boolean isEventBelongToOrganizerWithUid(Event event, UUID organizerUid) {
    Organizer organizer = event.getOrganizer();
    if (!organizer.getUid().equals(organizerUid)) {
      log.warn(String.format("Can not find any job-offer with id '%s' belong to organizer with id '%s'", event.getUid(), organizerUid));
      return false;
    }
    return true;
  }

  // Package //
  public boolean isPackageWithUidExist(Package packageTalent, UUID uid) {
    if (packageTalent == null) {
      log.warn(String.format("Can not find package with id '%s' ", uid));
      return false;
    }
    return true;
  }

  public boolean isPackageBelongToTalentWithUid(Package packageTalent, UUID talentId) {
    if (packageTalent.getTalent() == null) {
      log.warn(String.format("Can not find talent owning the package with id '%s'", packageTalent.getUid()));
      return false;
    }

    Talent talent = packageTalent.getTalent();
    if (!talent.getUid().equals(talentId)) {
      log.warn(String.format("There is no talent with id '%s' have the package with id '%s'", talentId, packageTalent.getUid()));
      return false;
    }
    return true;
  }
}
