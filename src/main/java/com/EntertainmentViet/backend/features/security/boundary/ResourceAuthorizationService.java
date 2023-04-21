package com.EntertainmentViet.backend.features.security.boundary;

import com.EntertainmentViet.backend.features.admin.api.*;
import com.EntertainmentViet.backend.features.aws.api.S3StorageController;
import com.EntertainmentViet.backend.features.booking.api.booking.OrganizerBookingController;
import com.EntertainmentViet.backend.features.booking.api.booking.TalentBookingController;
import com.EntertainmentViet.backend.features.booking.api.category.CategoryController;
import com.EntertainmentViet.backend.features.booking.api.location.LocationController;
import com.EntertainmentViet.backend.features.config.api.ConfigController;
import com.EntertainmentViet.backend.features.email.api.EmailController;
import com.EntertainmentViet.backend.features.email.api.EmailProcessController;
import com.EntertainmentViet.backend.features.finance.api.UserDealFeeRateController;
import com.EntertainmentViet.backend.features.notification.api.BookingNotificationController;
import com.EntertainmentViet.backend.features.organizer.api.event.EventController;
import com.EntertainmentViet.backend.features.organizer.api.event.EventPositionBookingController;
import com.EntertainmentViet.backend.features.organizer.api.event.EventPositionController;
import com.EntertainmentViet.backend.features.organizer.api.event.OrganizerEventController;
import com.EntertainmentViet.backend.features.organizer.api.feedback.OrganizerFeedbackController;
import com.EntertainmentViet.backend.features.organizer.api.joboffer.JobOfferController;
import com.EntertainmentViet.backend.features.organizer.api.organizer.OrganizerController;
import com.EntertainmentViet.backend.features.organizer.api.shoppingcart.ShoppingCartController;
import com.EntertainmentViet.backend.features.scoresystem.api.ScoreTypeController;
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
            // User fee mapping
            .mvcMatchers(HttpMethod.GET, anyPathAfter(UserDealFeeRateController.REQUEST_MAPPING_PATH))
            .hasAuthority(GenericUserRole.READ_USER_FEE.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(UserDealFeeRateController.REQUEST_MAPPING_PATH))
            .hasAuthority(GenericUserRole.UPDATE_USER_FEE.name())

            // User creation mapping
            .mvcMatchers(HttpMethod.POST, ofPath(UserController.REQUEST_MAPPING_PATH+UserController.ORGANIZER_PATH))
            .permitAll()
            .mvcMatchers(HttpMethod.POST, ofPath(UserController.REQUEST_MAPPING_PATH+UserController.TALENT_PATH))
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

            .mvcMatchers(HttpMethod.GET, ofPath(anyPathAfter(OrganizerEventController.REQUEST_MAPPING_PATH) + OrganizerEventController.BOOKING_PATH))
            .hasAuthority(EventRole.BROWSE_EVENT_BOOKING.name())
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

            .mvcMatchers(HttpMethod.POST, ofPath(anyPathAfter(OrganizerBookingController.REQUEST_MAPPING_PATH) + OrganizerBookingController.DONE_PATH))
            .hasAuthority(BookingRole.FINISH_BOOKING_ORGANIZER.name())

            // Organizer payment mapping
            .mvcMatchers(HttpMethod.GET, ofPath(anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH) + OrganizerController.CASH_PATH))
            .hasAuthority(PaymentRole.RECEIVE_ORGANIZER_CASH.name())
            .mvcMatchers(HttpMethod.POST, ofPath(anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH) + OrganizerController.CASH_PATH))
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
            .mvcMatchers(HttpMethod.GET, ofPath(anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH) + OrganizerController.CONFIDENTIAL_PATH))
            .hasAuthority(OrganizerRole.READ_ORGANIZER_CONFIDENTIAL.name())
            .mvcMatchers(HttpMethod.PUT, ofPath(anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH) + OrganizerController.CONFIDENTIAL_PATH))
            .hasAuthority(OrganizerRole.UPDATE_ORGANIZER_CONFIDENTIAL.name())

            // Organizer manage mapping
            .mvcMatchers(HttpMethod.POST, ofPath(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.VERIFY_ORGANIZER.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.READ_ORGANIZER.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(OrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(OrganizerRole.UPDATE_ORGANIZER.name())

            // Talent package order mapping
            .mvcMatchers(HttpMethod.POST, ofPath(PackageBookingController.REQUEST_MAPPING_PATH + PackageBookingController.CART_PATH))
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
            .mvcMatchers(HttpMethod.GET , ofPath(anyPathAfter(AdvertisementController.REQUEST_MAPPING_PATH) + AdvertisementController.CASH_PATH))
            .hasAuthority(PaymentRole.GET_ADVERTISEMENT_CASH.name())
            .mvcMatchers(HttpMethod.POST , ofPath(anyPathAfter(AdvertisementController.REQUEST_MAPPING_PATH) + AdvertisementController.CASH_PATH))
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

            .mvcMatchers(HttpMethod.POST, ofPath(anyPathAfter(TalentBookingController.REQUEST_MAPPING_PATH) + TalentBookingController.DONE_PATH))
            .hasAuthority(BookingRole.FINISH_BOOKING_TALENT.name())

            // Talent payment mapping
            .mvcMatchers(HttpMethod.GET, ofPath(anyPathAfter(TalentController.REQUEST_MAPPING_PATH) + TalentController.CASH_PATH))
            .hasAuthority(PaymentRole.RECEIVE_TALENT_CASH.name())
            .mvcMatchers(HttpMethod.POST, ofPath(anyPathAfter(TalentController.REQUEST_MAPPING_PATH) + TalentController.CASH_PATH))
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
            .mvcMatchers(HttpMethod.GET, ofPath(anyPathAfter(TalentController.REQUEST_MAPPING_PATH) + TalentController.CONFIDENTIAL_PATH))
            .hasAuthority(TalentRole.READ_TALENT_CONFIDENTIAL.name())
            .mvcMatchers(HttpMethod.PUT, ofPath(anyPathAfter(TalentController.REQUEST_MAPPING_PATH) + TalentController.CONFIDENTIAL_PATH))
            .hasAuthority(TalentRole.UPDATE_TALENT_CONFIDENTIAL.name())

            // Talent manage mapping
            .mvcMatchers(HttpMethod.POST, ofPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.VERIFY_TALENT.name())
            .mvcMatchers(HttpMethod.GET, ofPath(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.BROWSE_TALENT.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.READ_TALENT.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(TalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(TalentRole.UPDATE_TALENT.name())

            // Admin organizer mapping
            .mvcMatchers(HttpMethod.DELETE, ofPath(anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH) + AdminOrganizerController.DEACTIVE_PATH))
            .hasAuthority(AdminRole.ADMIN_DEACTIVE_ORGANIZER.name())

            .mvcMatchers(HttpMethod.GET, ofPath(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_ORGANIZER.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_READ_ORGANIZER.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_APPROVE_ORGANIZER.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_ORGANIZER.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(AdminOrganizerController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_REJECT_ORGANIZER.name())

            // Admin talent mapping
            .mvcMatchers(HttpMethod.DELETE, ofPath(anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH) + AdminTalentController.DEACTIVE_PATH))
            .hasAuthority(AdminRole.ADMIN_DEACTIVE_TALENT.name())

            .mvcMatchers(HttpMethod.GET, ofPath(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_TALENT.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_READ_TALENT.name())
            .mvcMatchers(HttpMethod.POST, anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_APPROVE_TALENT.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_TALENT.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(AdminTalentController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_REJECT_TALENT.name())

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
            .mvcMatchers(HttpMethod.GET, anyPathAfter(AdminBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_BROWSE_BOOKING.name())
            .mvcMatchers(HttpMethod.PUT, anyPathAfter(AdminBookingController.REQUEST_MAPPING_PATH))
            .hasAuthority(AdminRole.ADMIN_UPDATE_BOOKING.name())

            // Admin score mapping
            .mvcMatchers(HttpMethod.GET, ofPath(ScoreTypeController.REQUEST_MAPPING_PATH))
            .hasAuthority(ScoreTypeRole.READ_SCORE.name())
            .mvcMatchers(HttpMethod.POST, ofPath(ScoreTypeController.REQUEST_MAPPING_PATH))
            .hasAuthority(ScoreTypeRole.UPDATE_SCORE.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(ScoreTypeController.REQUEST_MAPPING_PATH))
            .hasAuthority(ScoreTypeRole.DELETE_SCORE.name())

            // Category mapping
            .mvcMatchers(HttpMethod.GET , ofPath(CategoryController.REQUEST_MAPPING_PATH))
            .hasAuthority(CategoryRole.READ_CATEGORY.name())
            .mvcMatchers(HttpMethod.POST , ofPath(CategoryController.REQUEST_MAPPING_PATH))
            .hasAuthority(CategoryRole.ADD_CATEGORY.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(CategoryController.REQUEST_MAPPING_PATH))
            .hasAuthority(CategoryRole.READ_CATEGORY.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(CategoryController.REQUEST_MAPPING_PATH))
            .hasAuthority(CategoryRole.DELETE_CATEGORY.name())

            // Location address mapping
            .mvcMatchers(HttpMethod.GET, ofPath(LocationController.REQUEST_MAPPING_PATH))
            .hasAuthority(LocationAddressRole.READ_LOCATION.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(LocationController.REQUEST_MAPPING_PATH))
            .hasAuthority(LocationAddressRole.READ_LOCATION.name())

            // Email mapping
            .mvcMatchers(HttpMethod.GET, ofPath(EmailController.REQUEST_MAPPING_PATH+ EmailController.VERIFICATION_PATH))
            .hasAuthority(EmailRole.VERIFY_EMAIL.name())
            .mvcMatchers(HttpMethod.GET, ofPath(EmailController.REQUEST_MAPPING_PATH+ EmailController.RESET_PASSWORD_PATH))
            .hasAuthority(EmailRole.RESET_PASSWORD.name())

            .mvcMatchers(HttpMethod.GET, ofPath(EmailProcessController.REQUEST_MAPPING_PATH+ EmailProcessController.VERIFICATION_PATH))
            .permitAll()
            .mvcMatchers(HttpMethod.GET, ofPath(EmailProcessController.REQUEST_MAPPING_PATH+ EmailProcessController.RESET_PASSWORD_PATH))
            .permitAll()

            // Spring docs mapping
            .mvcMatchers(HttpMethod.GET, anyPathAfter("/swagger-ui"))
            .permitAll()
            .mvcMatchers(HttpMethod.GET, ofPath("/api-docs"))
            .permitAll()
            .mvcMatchers(HttpMethod.GET, anyPathAfter("/api-docs"))
            .permitAll()

            // S3 mapping
            .mvcMatchers(HttpMethod.POST, ofPath(S3StorageController.REQUEST_MAPPING_PATH))
            .hasAuthority(S3StorageRole.UPLOAD_FILE.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(S3StorageController.REQUEST_MAPPING_PATH))
            .hasAuthority(S3StorageRole.READ_FILE.name())
            .mvcMatchers(HttpMethod.DELETE, anyPathAfter(S3StorageController.REQUEST_MAPPING_PATH))
            .hasAuthority(S3StorageRole.DELETE_FILE.name())

            // Config mapping
            .mvcMatchers(HttpMethod.POST, ofPath(ConfigController.REQUEST_MAPPING_PATH + ConfigController.FINANCE_PATH))
            .hasAuthority(ConfigRole.CONFIG_FINANCE.name())
            .mvcMatchers(HttpMethod.GET, anyPathAfter(ConfigController.REQUEST_MAPPING_PATH))
            .hasAuthority(ConfigRole.READ_CONFIG.name())

            // TODO assign specific role for this
            .mvcMatchers(HttpMethod.POST, ofPath(BookingNotificationController.REQUEST_MAPPING_PATH + BookingNotificationController.READ_PATH))
            .hasAuthority(NotificationRole.UPDATE_BOOKING_NOTI.name())
            .mvcMatchers(HttpMethod.GET, ofPath(BookingNotificationController.REQUEST_MAPPING_PATH + BookingNotificationController.LIST_PATH))
            .hasAuthority(NotificationRole.READ_BOOKING_NOTI.name())
            .mvcMatchers(HttpMethod.GET, ofPath(BookingNotificationController.REQUEST_MAPPING_PATH + BookingNotificationController.COUNT_PATH))
            .hasAuthority(NotificationRole.READ_BOOKING_NOTI.name())

            .mvcMatchers(HttpMethod.GET, ofPath(BookingNotificationController.REQUEST_MAPPING_PATH + "/new"))
            .hasAuthority(NotificationRole.READ_BOOKING_NOTI.name())

            .antMatchers("/ws/**").permitAll()
            .anyRequest().authenticated());
  }

  @Override
  public void ignoreCsrfPaths(CsrfConfigurer<HttpSecurity> csrfConfigurer) {
    csrfConfigurer.ignoringAntMatchers(
        anyPathAfter(UserController.REQUEST_MAPPING_PATH),
        anyPathAfter(EmailController.REQUEST_MAPPING_PATH),
        anyPathAfter("/ws/**")
    );
  }

  private String ofPath(String pattern) {
    return pattern.replaceAll("(?:\\{[a-zA-Z\\_]*\\})", "*");
  }

  private String anyPathAfter(String pattern) {
    return ofPath(pattern) + "/*";
  }
}
