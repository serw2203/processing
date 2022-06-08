package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class RestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RestApplication.class);
        context.close();
    }
}
