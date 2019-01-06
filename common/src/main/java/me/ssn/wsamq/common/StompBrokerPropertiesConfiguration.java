package me.ssn.wsamq.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author s.nechkin
 */
@PropertySource("classpath:stomp.config.properties")
@EnableConfigurationProperties(StompBrokerProperties.class)
@Configuration
public class StompBrokerPropertiesConfiguration {


}
