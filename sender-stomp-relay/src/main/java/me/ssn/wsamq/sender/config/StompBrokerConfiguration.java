package me.ssn.wsamq.sender.config;

import lombok.AllArgsConstructor;
import me.ssn.wsamq.common.StompBrokerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;

/**
 * @author s.nechkin
 */
@Configuration
@AllArgsConstructor
public class StompBrokerConfiguration extends AbstractMessageBrokerConfiguration {

    private final StompBrokerProperties stompBrokerProperties;

    @Override
    protected SimpUserRegistry createLocalUserRegistry(Integer order) {
        DefaultSimpUserRegistry registry = new DefaultSimpUserRegistry();
        if (order != null) {
            registry.setOrder(order);
        }
        return registry;
    }

    @Override
    protected void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/topic", "/exchange")
                // remove noise from heartbeats for debug purposes
                .setSystemHeartbeatSendInterval(0)
                .setSystemHeartbeatReceiveInterval(0)
                .setRelayHost(stompBrokerProperties.getHost())
                .setRelayPort(stompBrokerProperties.getPort())
                .setSystemLogin(stompBrokerProperties.getUser())
                .setSystemPasscode(stompBrokerProperties.getPassword())
                .setClientLogin(stompBrokerProperties.getUser())
                .setClientPasscode(stompBrokerProperties.getPassword())
                .setVirtualHost(stompBrokerProperties.getVirtualHost())
                .setUserRegistryBroadcast("/topic/simp-user-registry");
    }

}
