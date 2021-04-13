package com.epam.brest.rest.config;

import com.epam.brest.dao.jdbc.config.DbConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.epam.brest.service.daoImpl")
@Import(DbConfig.class)
public class RestRootConfig {
}
