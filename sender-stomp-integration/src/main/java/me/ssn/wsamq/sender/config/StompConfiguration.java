package me.ssn.wsamq.sender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.stomp.ReactorNettyTcpStompSessionManager;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.ReactorNettyTcpStompClient;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author s.nechkin
 */
@Configuration
@EnableIntegration
public class StompConfiguration {

    @Bean
    public ReactorNettyTcpStompClient stompClient() {
        ReactorNettyTcpStompClient stompClient = new ReactorNettyTcpStompClient("127.0.0.1", 61613);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        stompClient.setTaskScheduler(taskScheduler);
        stompClient.setReceiptTimeLimit(5000);
        return stompClient;
    }

    @Bean
    public StompSessionManager stompSessionManager() {
        ReactorNettyTcpStompSessionManager stompSessionManager = new ReactorNettyTcpStompSessionManager(stompClient());
        stompSessionManager.setAutoReceipt(true);
        StompHeaders headers = new StompHeaders();
        headers.add(StompHeaders.LOGIN, "rabbitmq");
        headers.add(StompHeaders.PASSCODE, "rabbitmq");
        stompSessionManager.setConnectHeaders(headers);
        return stompSessionManager;
    }
}
