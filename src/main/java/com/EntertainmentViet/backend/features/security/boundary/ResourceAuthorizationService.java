package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.features.admin.api.AdminAdvertisementController;
import com.EntertainmentViet.backend.features.admin.api.AdminOrganizerController;
import com.EntertainmentViet.backend.features.admin.api.AdminTalentController;
import com.EntertainmentViet.backend.features.admin.api.UserController;
import com.EntertainmentViet.backend.features.booking.api.CategoryController;
import com.EntertainmentViet.backend.features.booking.api.OrganizerBookingController;
import com.EntertainmentViet.backend.features.booking.api.TalentBookingController;
import com.EntertainmentViet.backend.features.organizer.api.EventBookingController;
import com.EntertainmentViet.backend.features.organizer.api.EventController;
import com.EntertainmentViet.backend.features.organizer.api.JobOfferController;
import com.EntertainmentViet.backend.features.organizer.api.OrganizerController;
import com.EntertainmentViet.backend.features.security.roles.AdminRole;
import com.EntertainmentViet.backend.features.security.roles.AdvertisementRole;
import com.EntertainmentViet.backend.features.security.roles.BookingRole;
import com.EntertainmentViet.backend.features.security.roles.CategoryRole;
import com.EntertainmentViet.backend.features.security.roles.EventApplicantRole;
import com.EntertainmentViet.backend.features.security.roles.EventRole;
import com.EntertainmentViet.backend.features.security.roles.JobOfferRole;
import com.EntertainmentViet.backend.features.security.roles.OrganizerRole;
import com.EntertainmentViet.backend.features.security.roles.PackageOrderRole;
import com.EntertainmentViet.backend.features.security.roles.PackageRole;
import com.EntertainmentViet.backend.features.security.roles.PaymentRole;
import com.EntertainmentViet.backend.features.security.roles.TalentRole;
import com.EntertainmentViet.backend.features.talent.api.AdvertisementController;
import com.EntertainmentViet.backend.features.talent.api.PackageBookingController;
import com.EntertainmentViet.backend.features.talent.api.PackageController;
import com.EntertainmentViet.backend.features.talent.api.TalentController;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;

@Service
public class ResourceAuthorizationService implements ResourceAuthorizationBoundary<HttpSecurity> {

  @Override
  public void authorizeRequests(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            // User creation mapping
            .mvcMatchers(HttpMethod.POST, ofPath(UserController.REQUEST_MAPPING_PATH))
            .permitAll()

            // Job offer mapping
            .mvcMatchers(HttpMethod.GET, ofPath(JobOfferController.REQUEST_MAPPING_PATH))
            .hasAuthority(JobOfferRole.BROWSE_JOBOFFER.name())
            .mvcMatchers(HttpMethod.POST, ofPath(JobOfferController.REQUEST_MAPPING_PATH))
            .hasAuthority(JobOfferRole.ADD_JOBOFFER.name())

            .mvcMatchers(HttpMethod.GET, anyPathAfter(JobOfferController.REQUEST_MAPPING_PATH))
            .hasAuthority(JobOfferRole.READ_JOBOFFER.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(JobOfferController.REQUEST_MAPPING_PATH))
            .hasAuthority(JobOfferRole.UPDATE_JOBOFFER.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(JobOfferController.REQUEST_MAPPING_PATH))
            .hasAuthority(JobOfferRole.DELETE_JOBOFFER.name())

            // Event applicant mapping
            .mvcMatchers(HttpMethod.POST, ofPath(EventBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventApplicantRole.APPLY_ORGANIZER_EVENT.name())
            .mvcMatchers(HttpMethod.GET, ofPath(EventBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventApplicantRole.BROWSE_EVENT_APPLICANT.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(EventBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventApplicantRole.ACCEPT_EVENT_APPLICANT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(EventBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventApplicantRole.REJECT_EVENT_APPLICANT.name())

            // Event mapping
            .mvcMatchers(HttpMethod.GET, ofPath(EventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.BROWSE_EVENT.name())
            .mvcMatchers(HttpMethod.POST, ofPath(EventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.ADD_EVENT.name())

            .mvcMatchers(HttpMethod.GET, anyPathAfter(EventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.READ_EVENT.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(EventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.UPDATE_EVENT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(EventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.DELETE_EVENT.name())

            // Organizer Booking mapping
            .mvcMatchers(HttpMethod.GET, ofPath(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.BROWSE_BOOKING_ORGANIZER.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.ACCEPT_BOOKING_ORGANIZER.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.CANCEL_BOOKING_ORGANIZER.name())

            // Organizer payment mapping
            .mvcMatchers(HttpMethod.GET, ofPaymentPath(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.RECEIVE_ORGANIZER_CASH.name())
            .mvcMatchers(HttpMethod.POST, ofPaymentPath(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.PAY_ORGANIZER_CASH.name())

            // Organizer manage mapping
            .mvcMatchers(HttpMethod.POST, ofPath(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.VERIFY_ORGANIZER.name())
            .mvcMatchers(HttpMethod.GET , anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.SELF_READ_ORGANIZER.name())
            .mvcMatchers(HttpMethod.PUT , anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.SELF_UPDATE_ORGANIZER.name())

            // Talent package order mapping
            .mvcMatchers(HttpMethod.POST , ofPath(PackageBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageOrderRole.ORDER_TALENT_PACKAGE.name())
            .mvcMatchers(HttpMethod.GET , ofPath(PackageBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageOrderRole.BROWSE_PACKAGE_ORDER.name())
            .mvcMatchers(HttpMethod.POST , anyPathAfter(PackageBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageOrderRole.ACCEPT_PACKAGE_ORDER.name())
            .mvcMatchers(HttpMethod.DELETE , anyPathAfter(PackageBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageOrderRole.REJECT_PACKAGE_ORDER.name())

            // Talent package mapping
            .mvcMatchers(HttpMethod.GET , ofPath(PackageController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageRole.BROWSE_PACKAGE.name())
            .mvcMatchers(HttpMethod.POST , ofPath(PackageController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageRole.ADD_PACKAGE.name())

            .mvcMatchers(HttpMethod.GET , anyPathAfter(PackageController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageRole.READ_PACKAGE.name())
            .mvcMatchers(HttpMethod.PUT , anyPathAfter(PackageController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageRole.UPDATE_PACKAGE.name())
            .mvcMatchers(HttpMethod.DELETE , anyPathAfter(PackageController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageRole.DELETE_PACKAGE.name())

            // Talent advertisement payment mapping
            .mvcMatchers(HttpMethod.GET , ofPaymentPath(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.GET_ADVERTISEMENT_CASH.name())
            .mvcMatchers(HttpMethod.POST , ofPaymentPath(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.PAY_ADVERTISEMENT_CASH.name())

            // Talent advertisement mapping
            .mvcMatchers(HttpMethod.GET , ofPath(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdvertisementRole.BROWSE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.POST , ofPath(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdvertisementRole.ADD_ADVERTISEMENT.name())

            .mvcMatchers(HttpMethod.GET , anyPathAfter(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdvertisementRole.READ_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.PUT , anyPathAfter(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdvertisementRole.UPDATE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.DELETE , anyPathAfter(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdvertisementRole.DELETE_ADVERTISEMENT.name())

            // Talent booking mapping
            .mvcMatchers(HttpMethod.GET , ofPath(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.BROWSE_BOOKING_TALENT.name())
            .mvcMatchers(HttpMethod.POST , anyPathAfter(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.ACCEPT_BOOKING_TALENT.name())
            .mvcMatchers(HttpMethod.DELETE , anyPathAfter(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.CANCEL_BOOKING_TALENT.name())

            // Talent payment mapping
            .mvcMatchers(HttpMethod.GET , ofPaymentPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.RECEIVE_TALENT_CASH.name())
            .mvcMatchers(HttpMethod.POST , ofPaymentPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.PAY_TALENT_CASH.name())

            // Talent manage mapping
            .mvcMatchers(HttpMethod.POST , ofPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.VERIFY_TALENT.name())
            .mvcMatchers(HttpMethod.GET , anyPathAfter(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.SELF_READ_TALENT.name())
            .mvcMatchers(HttpMethod.PUT , anyPathAfter(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.SELF_UPDATE_TALENT.name())

            // Admin organizer mapping
            .mvcMatchers(HttpMethod.GET , ofPath(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_ORGANIZER.name())
            .mvcMatchers(HttpMethod.POST , anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_APPROVE_ORGANIZER.name())
            .mvcMatchers(HttpMethod.PUT , anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_ORGANIZER.name())
            .mvcMatchers(HttpMethod.DELETE , anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_DEACTIVE_ORGANIZER.name())

            // Admin talent mapping
            .mvcMatchers(HttpMethod.GET , ofPath(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_TALENT.name())
            .mvcMatchers(HttpMethod.POST , anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_APPROVE_TALENT.name())
            .mvcMatchers(HttpMethod.PUT , anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_TALENT.name())
            .mvcMatchers(HttpMethod.DELETE , anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_DEACTIVE_TALENT.name())

            // Admin advertisement mapping
            .mvcMatchers(HttpMethod.GET , ofPath(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.POST , anyPathAfter(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_APPROVE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.PUT , anyPathAfter(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_DELETE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.DELETE , anyPathAfter(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_ADVERTISEMENT.name())

            // Category mapping
            .mvcMatchers(HttpMethod.GET , ofPath(CategoryController.REQUEST_MAPPING_PATH))
            .hasAuthority(CategoryRole.READ_CATEGORY.name())

            // Spring docs mapping
            .mvcMatchers(HttpMethod.GET, anyPathAfter("/swagger-ui"))
            .permitAll()
            .mvcMatchers(HttpMethod.GET, ofPath("/api-docs"))
            .permitAll()
            .mvcMatchers(HttpMethod.GET, anyPathAfter("/api-docs"))
            .permitAll()

            .anyRequest().authenticated());
  }

  private String ofPath(String pattern) {
    return pattern.replaceAll("(?:\\{[a-zA-Z\\_]*\\})", "*");
  }

  private String anyPathAfter(String pattern) {
    return ofPath(pattern) + "/*";
  }

  private String ofPaymentPath(String pattern) {
    return ofPath(pattern+"/cash");
  }
}
