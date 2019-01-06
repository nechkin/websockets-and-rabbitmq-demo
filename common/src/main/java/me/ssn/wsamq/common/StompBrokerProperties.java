package me.ssn.wsamq.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("stomp.broker")
public class StompBrokerProperties {
    private String host;
    private Integer port;
    private String user;
    private String password;
    private String virtualHost;
}
