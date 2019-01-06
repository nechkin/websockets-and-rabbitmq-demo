package me.ssn.wsamq.sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SenderRabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenderRabbitApplication.class, args);
    }

}
