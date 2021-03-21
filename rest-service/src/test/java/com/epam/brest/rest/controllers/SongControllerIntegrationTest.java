package com.epam.brest.rest.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.rest.config.RestDbConfigTest;
import com.epam.brest.rest.config.RestWebConfig;
import com.epam.brest.rest.daoImpl.SongDaoJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith({SpringExtension.class,MockitoExtension.class})
@WebAppConfiguration
@ContextConfiguration(classes = {RestWebConfig.class, RestDbConfigTest.class})
class SongControllerIntegrationTest {

    private final Song song;

    private final List<Song> list;

    private WebApplicationContext wac;

    private SongDaoJdbc songDaoJdbc = Mockito.mock(SongDaoJdbc.class);

    private MockMvc mockMvc;

    public SongControllerIntegrationTest(WebApplicationContext wac) throws Exception {
        Song song = new Song();
        song.setSongId(1);
        song.setSinger("Singer");
        song.setAlbum("Album");
        song.setTittle("Tittle");
        this.song = song;

        List<Song> list = new ArrayList<>();
        list.add(song);
        list.add(new Song());
        this.list = list;

        this.wac = wac;
        SongController songController = wac.getBean(SongController.class);
        Field songDaoJdbc = songController.getClass().getDeclaredField("songDaoJdbc");
        songDaoJdbc.setAccessible(true);
        songDaoJdbc.set(songController, this.songDaoJdbc);
    }

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void findAll() throws Exception {
        Mockito.when(this.songDaoJdbc.findAll()).thenReturn(list);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/songs")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].singer").value("Singer"));
    }

    @Test
    void findAllWithoutPlaylist() {
    }
}