package com.epam.brest.openapi;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SpringBootApplication
@ComponentScan(basePackages = "com.epam.brest")
public class ApplicationOpenAPI {

    public static void main(String[] args) {
        SpringApplication.run (ApplicationOpenAPI.class, args);
    }

    @Bean
    public ObjectMapper objectMapper(){
        var objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);

        return objectMapper;
    }
}
