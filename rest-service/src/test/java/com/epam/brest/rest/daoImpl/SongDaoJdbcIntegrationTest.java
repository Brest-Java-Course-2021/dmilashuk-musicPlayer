package com.epam.brest.rest.daoImpl;

import com.epam.brest.rest.config.RestDbConfigTest;
import com.epam.brest.model.Song;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitConfig(RestDbConfigTest.class)
public class SongDaoJdbcIntegrationTest {

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
        assertEquals(5,songDaoJdbc.findAll().size());
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findAllWithoutPlaylistTest() {
        assertEquals(3,songDaoJdbc.findAllWithoutPlaylist(1).size());
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findByFilterTest() {
        Date startDate = new Date(new GregorianCalendar(1980, Calendar.AUGUST,22).getTime().getTime());
        Date endDate = new Date(new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime().getTime());
        assertEquals(2, songDaoJdbc.findAllByFilter(startDate, endDate).size());
        assertEquals(1,songDaoJdbc.findAllByFilter(startDate, null).size());
        assertEquals(1,songDaoJdbc.findAllByFilter(null, startDate).size());
        assertEquals(1,songDaoJdbc.findAllByFilter(startDate, startDate).size());
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void findByIdTest() {
        Optional<Song> result = songDaoJdbc.findById(3);
        assertTrue(result.isPresent());
        assertEquals(3,result.get().getSongId());
        assertTrue(songDaoJdbc.findById(999).isEmpty());
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void createTest() {
        Song song1 = new Song();
        song1.setSinger("New singer");
        song1.setTittle("New tittle");
        assertEquals(6, songDaoJdbc.create(song1));
        Song song2 = new Song();
        song2.setSinger("Busova");
        assertThrows(IllegalArgumentException.class, () -> songDaoJdbc.create(song2));
        Song song3 = new Song();
        song3.setTittle("Polovinu");
        assertThrows(IllegalArgumentException.class, () -> songDaoJdbc.create(song3));
        Song song4 = new Song();
        song4.setSinger("Metallica");
        song4.setTittle("Nothing else matters");
        assertThrows(IllegalArgumentException.class, () -> songDaoJdbc.create(song4));
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void updateTest() {
        Song song1 = new Song();
        song1.setSinger("Алла Пугачева");
        song1.setTittle("Все могут короли");
        song1.setSongId(2);
        assertEquals(1, songDaoJdbc.update(song1));
        assertEquals("Алла Пугачева",songDaoJdbc.findById(2).orElseThrow().getSinger());
        Song song2 = new Song();
        song2.setSinger("Busova");
        assertThrows(IllegalArgumentException.class, () -> songDaoJdbc.update(song2));
        Song song3 = new Song();
        song3.setTittle("Polovinu");
        assertThrows(IllegalArgumentException.class, () -> songDaoJdbc.update(song3));
        Song song4 = new Song();
        song4.setSinger("Metallica");
        song4.setTittle("Nothing else matters");
        assertThrows(IllegalArgumentException.class, () -> songDaoJdbc.update(song4));
    }

    @Test
    @SqlGroup({
            @Sql(value = "classpath:init-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "classpath:clean-up-test-db.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    public void deleteTest() {
        assertEquals(1, songDaoJdbc.delete(1));
        assertEquals(4,songDaoJdbc.findAll().size());
        assertEquals(0, songDaoJdbc.delete(999));
    }

}