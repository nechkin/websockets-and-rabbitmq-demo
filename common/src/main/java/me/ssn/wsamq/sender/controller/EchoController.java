package me.ssn.wsamq.sender.controller;

import lombok.AllArgsConstructor;
import me.ssn.wsamq.common.CustomHeaders;
import me.ssn.wsamq.sender.service.EchoMessageHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author s.nechkin
 */
@RestController
@RequestMapping(value = "/echo")
@AllArgsConstructor
public class EchoController {

    private final EchoMessageHandler echoMessageHandler;

    @PostMapping(consumes = "text/plain; charset=UTF-8")
    public void echo(@RequestHeader(CustomHeaders.HEADER_USERNAME) String username,
                     @RequestHeader(CustomHeaders.HEADER_SCOKET_SESSION_ID) String socketSession,
                     @RequestBody String message) {
        echoMessageHandler.handle(username, socketSession, message);
    }
}
