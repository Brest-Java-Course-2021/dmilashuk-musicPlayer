package com.epam.brest.rest.config;

import com.epam.brest.testDb.config.TestDbConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Import(TestDbConfig.class)
@ComponentScan(basePackages = "com.epam.brest.rest.daoImpl")
@PropertySource("classpath:jdbc.properties")
public class RestDbConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestDbConfigTest.class);

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