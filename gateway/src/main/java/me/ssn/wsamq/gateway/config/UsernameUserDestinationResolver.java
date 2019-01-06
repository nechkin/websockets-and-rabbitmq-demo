package me.ssn.wsamq.gateway.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResult;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Collections;

public class UsernameUserDestinationResolver extends DefaultUserDestinationResolver {

    public UsernameUserDestinationResolver(SimpUserRegistry userRegistry) {
        super(userRegistry);
    }

    @Override
    public UserDestinationResult resolveDestination(Message<?> message) {
        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message, SimpMessageHeaderAccessor.class);

        if (accessor == null) {
            return super.resolveDestination(message);
        }

        final String destination = accessor.getDestination();
        final String username = accessor.getUser() != null ? accessor.getUser().getName() : null;

        if (destination != null && destination.endsWith("echo.user")) {
            if (username != null) {
                String targetDestination = String.format("/exchange/amq.direct/echo.user-user%s", username);
                return new UserDestinationResult(destination, Collections.singleton(targetDestination),
                        destination, username);
            }
        }

        return super.resolveDestination(message);
    }
}
