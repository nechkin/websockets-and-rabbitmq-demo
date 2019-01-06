package me.ssn.wsamq.gateway;

import me.ssn.wsamq.common.StompBrokerPropertiesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author s.nechkin
 */
@Import(StompBrokerPropertiesConfiguration.class)
@SpringBootApplication
public class GatewayApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/index.html")
                .setCachePeriod(0);
    }

}

