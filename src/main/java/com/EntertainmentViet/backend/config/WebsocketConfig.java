package com.EntertainmentViet.backend.config;

import com.EntertainmentViet.backend.config.constants.AppConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

  private final JwtDecoder jwtDecoder;

  private final SecurityConfig.CustomJwtAuthenticationConverter jwtAuthenticationConverter;

  @Override
  public void configureMessageBroker(final MessageBrokerRegistry registry) {
    registry.enableSimpleBroker(AppConstant.NOTIFICATION_BASE_TOPIC, "/user");
    registry.setApplicationDestinationPrefixes("/app-notification");
    registry.setUserDestinationPrefix("/user");
  }

  @Override
  public void registerStompEndpoints(final StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();

        if (command != null && (command.equals(StompCommand.CONNECT))) {
          String token = accessor.getFirstNativeHeader("token");
          Jwt jwt = jwtDecoder.decode(token);
          Principal authentication = jwtAuthenticationConverter.convert(jwt);
          accessor.setUser(authentication);
        }
        return message;
      }
    });
  }
}
