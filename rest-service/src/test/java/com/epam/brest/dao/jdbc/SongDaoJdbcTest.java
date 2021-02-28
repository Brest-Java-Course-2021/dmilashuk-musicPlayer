package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.config.TestRestDbConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestRestDbConfig.class)
public class SongDaoJdbcTest {

    @Autowired
    private SongDaoJdbc songDaoJdbc;

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findAllTest() {

    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findByFilterTest() {
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testFindByFilterTest() {
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void testFindByFilter1Test() {
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findByIdTest() {
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void createTest() {
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void updateTest() {
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void deleteTest() {
    }
}