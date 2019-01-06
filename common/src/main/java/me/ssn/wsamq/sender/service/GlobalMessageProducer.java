package me.ssn.wsamq.sender.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GlobalMessageProducer {

    private final EchoMessageHandler messageHandler;

    public GlobalMessageProducer(EchoMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Scheduled(fixedRate = 1000)
    public void emitGlobalNotification() {
        messageHandler.handleGloabl("Global");
    }
}
