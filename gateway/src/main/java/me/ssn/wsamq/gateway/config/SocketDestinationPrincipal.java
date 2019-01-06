package me.ssn.wsamq.gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.user.DestinationUserNameProvider;

import java.security.Principal;

/**
 * @author s.nechkin
 */
@AllArgsConstructor
public class SocketDestinationPrincipal implements Principal, DestinationUserNameProvider {

    private final String name;
    private final String socketSessionId;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDestinationUserName() {
        return socketSessionId;
    }
}
