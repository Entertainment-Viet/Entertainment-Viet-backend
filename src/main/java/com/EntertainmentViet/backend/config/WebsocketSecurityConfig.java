package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.features.security.roles.BookingRole;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages.anyMessage().hasAuthority(BookingRole.READ_BOOKING.name());
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }
}
