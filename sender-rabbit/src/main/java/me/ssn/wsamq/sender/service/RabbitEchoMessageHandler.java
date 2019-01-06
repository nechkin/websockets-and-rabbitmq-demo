package me.ssn.wsamq.sender.service;

import lombok.extern.slf4j.Slf4j;
import me.ssn.wsamq.dto.MessageOrigin;
import me.ssn.wsamq.dto.ServerMessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author s.nechkin
 */
@Slf4j
@Service
public class RabbitEchoMessageHandler implements EchoMessageHandler {

    private final RabbitTemplate rabbitTemplate;

    public RabbitEchoMessageHandler(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void handle(String username, String socketSessionId, String message) {
        final ServerMessageDto echo = new ServerMessageDto(MessageOrigin.RABBIT, System.currentTimeMillis(), message);
        rabbitTemplate.convertAndSend("amq.direct", "echo.user-user" + username, echo);
        rabbitTemplate.convertAndSend("amq.direct", "echo.window-user" + socketSessionId, echo);
    }

    @Override
    public void handleGloabl(String message) {
        ServerMessageDto messageDto = new ServerMessageDto(MessageOrigin.RABBIT, System.currentTimeMillis(), message);
        rabbitTemplate.convertAndSend("amq.topic", "global", messageDto);
    }
}
