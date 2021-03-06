package com.epam.brest.dao.jdbc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.epam.brest")
public class TestJdbcDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestJdbcDbConfig.class);

    @Autowired
    private DataSource dataSource;

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        LOGGER.debug("Bean namedParameterJdbcTemplate was created");
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager transactionManager(){
        LOGGER.debug("Bean transactionManager was created");
        return new DataSourceTransactionManager(dataSource);
    }
}
