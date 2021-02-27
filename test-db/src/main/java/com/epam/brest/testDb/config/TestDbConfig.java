package com.epam.brest.testDb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class TestDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDbConfig.class);

    @Bean
    public DataSource dataSource(){
        try {
            EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
            LOGGER.debug("DBCP-Test DataSource was created");
            return dbBuilder.setType(EmbeddedDatabaseType.H2)
                    .addScript("classpath:schema.sql")
                    .build();
        } catch (Exception e){
            LOGGER.error("Embedded DataSource cannot be created", e);
            return null;
        }
    }

}
