package me.ssn.wsamq.sender.service;

import lombok.extern.slf4j.Slf4j;
import me.ssn.wsamq.dto.MessageOrigin;
import me.ssn.wsamq.dto.ServerMessageDto;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;

/**
 * @author s.nechkin
 */
@Slf4j
@Service
public class StompRelayEchoMessageHandler implements EchoMessageHandler, ApplicationListener<BrokerAvailabilityEvent> {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private boolean brokerAvailable;

    public StompRelayEchoMessageHandler(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void handle(String username, String socketSessionId, String message) {
        if (brokerAvailable) {
            final ServerMessageDto echo = new ServerMessageDto(MessageOrigin.RELAY, System.currentTimeMillis(), message);
            simpMessagingTemplate.convertAndSendToUser(username, "/exchange/amq.direct/echo.user.StompRelay", echo);
            simpMessagingTemplate.convertAndSendToUser(socketSessionId, "/exchange/amq.direct/echo.window", echo);
        } else {
            log.warn("Broker is not yet available");
        }
    }

    @Override
    public void handleGlobal(String message) {
        if (brokerAvailable) {
            ServerMessageDto messageDto = new ServerMessageDto(MessageOrigin.RELAY, System.currentTimeMillis(), message);
            simpMessagingTemplate.convertAndSend("/topic/global", messageDto);
        } else {
            log.warn("Broker is not yet available");
        }
    }

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent brokerAvailabilityEvent) {
        brokerAvailable = brokerAvailabilityEvent.isBrokerAvailable();
    }
}
