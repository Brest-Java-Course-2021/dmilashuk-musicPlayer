package com.epam.brest.kafka.consumerDao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.epam.brest")
public class ApplicationSongConsumer {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationSongConsumer.class, args);
    }

}
