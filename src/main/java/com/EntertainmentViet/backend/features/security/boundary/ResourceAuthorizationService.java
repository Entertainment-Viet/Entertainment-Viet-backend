package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.features.admin.api.AdminAdvertisementController;
import com.EntertainmentViet.backend.features.admin.api.AdminBookingController;
import com.EntertainmentViet.backend.features.admin.api.AdminOrganizerController;
import com.EntertainmentViet.backend.features.admin.api.UserController;
import com.EntertainmentViet.backend.features.admin.api.talent.AdminTalentController;
import com.EntertainmentViet.backend.features.booking.api.booking.OrganizerBookingController;
import com.EntertainmentViet.backend.features.booking.api.booking.TalentBookingController;
import com.EntertainmentViet.backend.features.booking.api.category.CategoryController;
import com.EntertainmentViet.backend.features.organizer.api.event.EventController;
import com.EntertainmentViet.backend.features.organizer.api.event.EventPositionBookingController;
import com.EntertainmentViet.backend.features.organizer.api.event.EventPositionController;
import com.EntertainmentViet.backend.features.organizer.api.event.OrganizerEventController;
import com.EntertainmentViet.backend.features.organizer.api.feedback.OrganizerFeedbackController;
import com.EntertainmentViet.backend.features.organizer.api.joboffer.JobOfferController;
import com.EntertainmentViet.backend.features.organizer.api.organizer.OrganizerController;
import com.EntertainmentViet.backend.features.organizer.api.shoppingcart.ShoppingCartController;
import com.EntertainmentViet.backend.features.security.roles.*;
import com.EntertainmentViet.backend.features.talent.api.advertisement.AdvertisementController;
import com.EntertainmentViet.backend.features.talent.api.feedback.TalentFeedbackController;
import com.EntertainmentViet.backend.features.talent.api.packagetalent.PackageBookingController;
import com.EntertainmentViet.backend.features.talent.api.packagetalent.PackageController;
import com.EntertainmentViet.backend.features.talent.api.talent.TalentController;
import com.EntertainmentViet.backend.features.talent.api.talent.TalentReviewController;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.stereotype.Service;

@Service
public class ResourceAuthorizationService implements ResourceAuthorizationBoundary<HttpSecurity> {

  @Override
  public void authorizeRequests(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            // User creation mapping
            .mvcMatchers(HttpMethod.POST, anyPathAfter(UserController.REQUEST_MAPPING_PATH))
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

            // Event Position applicant mapping
            .mvcMatchers(HttpMethod.POST, ofPath(EventPositionBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PositionApplicantRole.APPLY_ORGANIZER_POSITION.name())
            .mvcMatchers(HttpMethod.GET, ofPath(EventPositionBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PositionApplicantRole.BROWSE_POSITION_APPLICANT.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(EventPositionBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PositionApplicantRole.ACCEPT_POSITION_APPLICANT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(EventPositionBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(PositionApplicantRole.REJECT_POSITION_APPLICANT.name())

            // Event position mapping
            .mvcMatchers(HttpMethod.GET, ofPath(EventPositionController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventPositionRole.BROWSE_EVENT_POSITION.name())
            .mvcMatchers(HttpMethod.POST, ofPath(EventPositionController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventPositionRole.ADD_EVENT_POSITION.name())

            .mvcMatchers(HttpMethod.GET, anyPathAfter(EventPositionController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventPositionRole.READ_EVENT_POSITION.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(EventPositionController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventPositionRole.UPDATE_EVENT_POSITION.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(EventPositionController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventPositionRole.DELETE_EVENT_POSITION.name())


            // Event mapping
            .mvcMatchers(HttpMethod.GET, ofPath(OrganizerEventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.BROWSE_EVENT.name())
            .mvcMatchers(HttpMethod.POST, ofPath(OrganizerEventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.ADD_EVENT.name())

            .mvcMatchers(HttpMethod.GET, anyPathAfter(OrganizerEventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.READ_EVENT.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(OrganizerEventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.UPDATE_EVENT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(OrganizerEventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.DELETE_EVENT.name())

            .mvcMatchers(HttpMethod.GET, ofPath(EventController.REQUEST_MAPPING_PATH))
            .hasAuthority(EventRole.BROWSE_EVENT.name())

            // Organizer Booking mapping
            .mvcMatchers(HttpMethod.GET, ofPath(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.BROWSE_BOOKING_ORGANIZER.name())
            .mvcMatchers(HttpMethod.POST, ofPath(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.ADD_BOOKING.name())

            .mvcMatchers(HttpMethod.GET, anyPathAfter(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.READ_BOOKING.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.UPDATE_BOOKING.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.ACCEPT_BOOKING_ORGANIZER.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(OrganizerBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.CANCEL_BOOKING_ORGANIZER.name())

            .mvcMatchers(HttpMethod.POST, ofPath(OrganizerBookingController.REQUEST_MAPPING_PATH + "/{id}/done"))
            .hasAuthority(BookingRole.FINISH_BOOKING_ORGANIZER.name())

            // Organizer payment mapping
            .mvcMatchers(HttpMethod.GET, ofPaymentPath(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.RECEIVE_ORGANIZER_CASH.name())
            .mvcMatchers(HttpMethod.POST, ofPaymentPath(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.PAY_ORGANIZER_CASH.name())

            // Organizer shopping chart
            .mvcMatchers(HttpMethod.GET, ofPath(ShoppingCartController.REQUEST_MAPPING_PATH))
            .hasAuthority(ShoppingCartRole.READ_SHOPPING_CART.name())
            .mvcMatchers(HttpMethod.POST, ofPath(ShoppingCartController.REQUEST_MAPPING_PATH))
            .hasAuthority(PackageOrderRole.ORDER_TALENT_PACKAGE.name())
            .mvcMatchers(HttpMethod.DELETE, ofPath(ShoppingCartController.REQUEST_MAPPING_PATH))
            .hasAuthority(ShoppingCartRole.CLEAR_SHOPPING_CART.name())

            .mvcMatchers(HttpMethod.GET, anyPathAfter(ShoppingCartController.REQUEST_MAPPING_PATH))
            .hasAuthority(ShoppingCartRole.READ_CART_ITEM.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(ShoppingCartController.REQUEST_MAPPING_PATH))
            .hasAuthority(ShoppingCartRole.UPDATE_CART_ITEM.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(ShoppingCartController.REQUEST_MAPPING_PATH))
            .hasAuthority(ShoppingCartRole.DELETE_CART_ITEM.name())

            // Organizer feedback mapping
            .mvcMatchers(HttpMethod.GET, ofPath(OrganizerFeedbackController.REQUEST_MAPPING_PATH))
            .hasAuthority(FeedbackRole.BROWSE_ORGANIZER_FEEDBACK.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(OrganizerFeedbackController.REQUEST_MAPPING_PATH))
            .hasAuthority(FeedbackRole.READ_ORGANIZER_FEEDBACK.name())
            .mvcMatchers(HttpMethod.POST, ofPath(OrganizerFeedbackController.REQUEST_MAPPING_PATH))
            .hasAuthority(FeedbackRole.ADD_ORGANIZER_FEEDBACK.name())

            // Organizer confidential mapping
            .mvcMatchers(HttpMethod.GET, ofPath(OrganizerController.REQUEST_MAPPING_PATH + "/confidential"))
            .hasAuthority(OrganizerRole.READ_ORGANIZER_DETAIL.name())
            .mvcMatchers(HttpMethod.PUT, ofPath(OrganizerController.REQUEST_MAPPING_PATH + "/confidential"))
            .hasAuthority(OrganizerRole.SELF_UPDATE_ORGANIZER.name())

            // Organizer manage mapping
            .mvcMatchers(HttpMethod.POST, ofPath(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.VERIFY_ORGANIZER.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.READ_ORGANIZER.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.SELF_UPDATE_ORGANIZER.name())

            // Talent package order mapping
            .mvcMatchers(HttpMethod.POST, ofPath(PackageBookingController.REQUEST_MAPPING_PATH + "/shoppingcart"))
            .hasAuthority(ShoppingCartRole.ADD_CART_ITEM.name())

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
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(AdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdvertisementRole.DELETE_ADVERTISEMENT.name())

            // Talent booking mapping
            .mvcMatchers(HttpMethod.GET, ofPath(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.BROWSE_BOOKING_TALENT.name())
            .mvcMatchers(HttpMethod.POST, ofPath(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.ADD_BOOKING.name())

            .mvcMatchers(HttpMethod.GET, anyPathAfter(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.READ_BOOKING.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.UPDATE_BOOKING.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.ACCEPT_BOOKING_TALENT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(TalentBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(BookingRole.CANCEL_BOOKING_TALENT.name())

            .mvcMatchers(HttpMethod.POST, ofPath(TalentBookingController.REQUEST_MAPPING_PATH + "/{id}/done"))
            .hasAuthority(BookingRole.FINISH_BOOKING_TALENT.name())

            // Talent payment mapping
            .mvcMatchers(HttpMethod.GET, ofPaymentPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.RECEIVE_TALENT_CASH.name())
            .mvcMatchers(HttpMethod.POST, ofPaymentPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(PaymentRole.PAY_TALENT_CASH.name())

            // Talent feedback mapping
            .mvcMatchers(HttpMethod.GET, ofPath(TalentFeedbackController.REQUEST_MAPPING_PATH))
            .hasAuthority(FeedbackRole.BROWSE_TALENT_FEEDBACK.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(TalentFeedbackController.REQUEST_MAPPING_PATH))
            .hasAuthority(FeedbackRole.READ_TALENT_FEEDBACK.name())
            .mvcMatchers(HttpMethod.POST, ofPath(TalentFeedbackController.REQUEST_MAPPING_PATH))
            .hasAuthority(FeedbackRole.ADD_TALENT_FEEDBACK.name())

            // Talent review
            .mvcMatchers(HttpMethod.GET, ofPath(TalentReviewController.REQUEST_MAPPING_PATH))
            .hasAuthority(ReviewRole.READ_REVIEW.name())
            .mvcMatchers(HttpMethod.POST, ofPath(TalentReviewController.REQUEST_MAPPING_PATH))
            .hasAuthority(ReviewRole.ADD_REVIEW.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(TalentReviewController.REQUEST_MAPPING_PATH))
            .hasAuthority(ReviewRole.READ_REVIEW.name())

            // Talent confidential mapping
            .mvcMatchers(HttpMethod.GET, ofPath(TalentController.REQUEST_MAPPING_PATH + "/confidential"))
            .hasAuthority(TalentRole.READ_TALENT_DETAIL.name())
            .mvcMatchers(HttpMethod.PUT, ofPath(TalentController.REQUEST_MAPPING_PATH + "/confidential"))
            .hasAuthority(TalentRole.SELF_UPDATE_TALENT.name())

            // Talent manage mapping
            .mvcMatchers(HttpMethod.POST , ofPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.VERIFY_TALENT.name())
            .mvcMatchers(HttpMethod.GET , ofPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.BROWSE_TALENT.name())
            .mvcMatchers(HttpMethod.GET , anyPathAfter(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.READ_TALENT.name())
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
            .mvcMatchers(HttpMethod.GET, ofPath(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_TALENT.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_APPROVE_TALENT.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_TALENT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_DEACTIVE_TALENT.name())

            // Admin advertisement mapping
            .mvcMatchers(HttpMethod.GET, ofPath(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_APPROVE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_DELETE_ADVERTISEMENT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(AdminAdvertisementController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_ADVERTISEMENT.name())

            // Admin booking mapping
            .mvcMatchers(HttpMethod.GET, ofPath(AdminBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_BOOKING.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(AdminBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_BOOKING.name())

            // Category mapping
            .mvcMatchers(HttpMethod.GET , ofPath(CategoryController.REQUEST_MAPPING_PATH))
            .hasAuthority(CategoryRole.READ_CATEGORY.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(CategoryController.REQUEST_MAPPING_PATH))
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

  @Override
  public void ignoreCsrfPaths(CsrfConfigurer<HttpSecurity> csrfConfigurer) {
    csrfConfigurer.ignoringAntMatchers(
        anyPathAfter(UserController.REQUEST_MAPPING_PATH)
    );
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
