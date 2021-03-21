package com.epam.brest.rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:jdbc.properties")
})
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.epam.brest.rest.daoImpl")
public class RestDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestDbConfig.class);

    @Value("${jdbc.driverClassName}")
    private String driverClassname;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.userName}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Bean
    DataSource dataSource(){
        try {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName(driverClassname);
            driverManagerDataSource.setUrl(url);
            driverManagerDataSource.setUsername(userName);
            driverManagerDataSource.setPassword(password);
            LOGGER.debug("DBCP DataSource was created");
            return driverManagerDataSource;
        }catch (Exception e){
            LOGGER.error("DBCP DataSource bean cannot be created",e);
            return null;
        }
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        LOGGER.debug("Bean namedParameterJdbcTemplate was created");
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    PlatformTransactionManager transactionManager(){
        LOGGER.debug("Bean transactionManager was created");
        return new DataSourceTransactionManager(dataSource());
    }
}