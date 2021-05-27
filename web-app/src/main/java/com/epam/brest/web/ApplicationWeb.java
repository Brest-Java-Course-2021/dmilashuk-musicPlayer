package com.epam.brest.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = "com.epam.brest")
@PropertySource("classpath:restServer.properties")
public class ApplicationWeb implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationWeb.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/","/playlists");
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

}
