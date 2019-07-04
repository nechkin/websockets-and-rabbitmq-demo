package me.ssn.wsamq.sender.service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.ssn.wsamq.dto.MessageOrigin;
import me.ssn.wsamq.dto.ServerMessageDto;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author s.nechkin
 */
@Slf4j
@Service
public class StompIntegrationEchoMessageHandler implements EchoMessageHandler {

    private StompSession stompSession;

    private final StompSessionManager stompSessionManager;

    public StompIntegrationEchoMessageHandler(StompSessionManager stompSessionManager) {
        this.stompSessionManager = stompSessionManager;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        stompSessionManager.connect(new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                StompIntegrationEchoMessageHandler.this.stompSession = session;
            }
        });
    }

    @Override
    public void handle(String username, String socketSessionId, String message) {
        if (stompSession != null && stompSession.isConnected()) {
            final ServerMessageDto echo = new ServerMessageDto(MessageOrigin.INTEGRATION, System.currentTimeMillis(),
                    message);
            stompSession.send("/exchange/amq.direct/echo.user-user" + username, echo);
            stompSession.send("/exchange/amq.direct/echo.window-user" + socketSessionId, echo);
        } else {
            log.warn("Session is not yet ready");
        }
    }

    @Override
    public void handleGlobal(String message) {
        if (stompSession != null && stompSession.isConnected()) {
            ServerMessageDto messageDto = new ServerMessageDto(MessageOrigin.INTEGRATION, System.currentTimeMillis(),
                    message);
            stompSession.send("/topic/global", messageDto);
        } else {
            log.warn("Session is not yet ready");
        }
    }

    //
    private void convertAndSend(Object payload, StompSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        JsonEncoding encoding = JsonEncoding.UTF8;
        try {
            JsonGenerator generator = objectMapper.getFactory().createGenerator(out, encoding);
            objectMapper.writeValue(generator, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Map<String, Object> headers = new HashMap<>();
        // headers.put("simpMessageType", SimpMessageType.MESSAGE);
        // headers.put("stompCommand", StompCommand.SEND);
        // headers.put("simpDestination", "/topic/global");

        session.send("/topic/global", out.toByteArray());
    }
}
