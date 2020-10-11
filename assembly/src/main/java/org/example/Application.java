package org.example;

import com.google.common.eventbus.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Bean
    public EventBus bus() {
        return new EventBus();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
