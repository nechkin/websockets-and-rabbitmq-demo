package me.ssn.wsamq.sender;

import me.ssn.wsamq.common.StompBrokerPropertiesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import(StompBrokerPropertiesConfiguration.class)
@EnableScheduling
@SpringBootApplication
public class SenderStompRelayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenderStompRelayApplication.class, args);
    }

}
