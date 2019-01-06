package me.ssn.wsamq.gateway.config;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * @author s.nechkin
 */
// @Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // Prevent users sending messages directly to topics and queues, only "app" destination
                .simpMessageDestMatchers("/topic/**", "/exchange/**").denyAll()
                // Prevent users from subscribing to private queues
                .simpSubscribeDestMatchers("/topic/**/*-user*", "/exchange/**/*-user*").denyAll()
                .anyMessage().hasRole("USER");
    }
}
