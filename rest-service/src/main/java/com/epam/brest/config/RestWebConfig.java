package com.epam.brest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.brest.controllers")
public class RestWebConfig implements WebMvcConfigurer {}
