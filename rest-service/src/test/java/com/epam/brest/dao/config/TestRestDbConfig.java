package com.epam.brest.dao.config;

import com.epam.brest.testDb.config.TestDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Import(TestDbConfig.class)
@ComponentScan(basePackages = "com.epam.brest.dao")
public class TestRestDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRestDbConfig.class);

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
