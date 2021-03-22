package com.epam.brest.rest.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.rest.config.RestDbConfigTest;
import com.epam.brest.rest.config.RestWebConfig;
import com.epam.brest.rest.daoImpl.SongDaoJdbc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith({SpringExtension.class,MockitoExtension.class})
@WebAppConfiguration
@ContextConfiguration(classes = {RestWebConfig.class, RestDbConfigTest.class})
class SongControllerIntegrationTest {

    private final List<Song> list;

    private final Song song;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private SongDaoJdbc songDaoJdbc;

    private MockMvc mockMvc;

    {
        Song song1 = new Song();
        song1.setSinger("Singer1");
        Song song2 = new Song();
        song2.setSinger("Singer2");
        List<Song> list = new ArrayList<>();
        list.add(song1);
        list.add(song2);
        this.list = list;
        this.song = song1;
    }

    @BeforeEach
    public void setup(){
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new SongController(songDaoJdbc)).build();
    }

    @Test
    void findAllWithoutRequestParamTest() throws Exception {
        when(this.songDaoJdbc.findAll()).thenReturn(list);
        String responseBody = mockMvc.perform(get("/songs")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList);
        verify(songDaoJdbc).findAll();
        verifyNoMoreInteractions(songDaoJdbc);
    }

    @Test
    void findAllWithRequestParamTest() throws Exception{
        when(this.songDaoJdbc.findAllByFilter(any(Date.class), any(Date.class))).thenReturn(list);
        String responseBody = mockMvc.perform(get("/songs?startDate=1992-03-12&endDate=2000-03-12")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList);
        verify(songDaoJdbc).findAllByFilter(any(Date.class), any(Date.class));
        verifyNoMoreInteractions(songDaoJdbc);

        when(this.songDaoJdbc.findAllByFilter(any(Date.class), isNull())).thenReturn(list);
        String responseBody1 = mockMvc.perform(get("/songs?startDate=1992-03-12")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList1 = objectMapper.readValue(
                responseBody1,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList1);
        verify(songDaoJdbc).findAllByFilter(any(Date.class), isNull());
        verifyNoMoreInteractions(songDaoJdbc);

        when(this.songDaoJdbc.findAllByFilter(isNull(), any(Date.class))).thenReturn(list);
        String responseBody2 = mockMvc.perform(get("/songs?endDate=1992-03-12")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList2 = objectMapper.readValue(
                responseBody2,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList2);
        verify(songDaoJdbc).findAllByFilter(any(Date.class), isNull());
        verifyNoMoreInteractions(songDaoJdbc);
    }

    @Test
    void findAllWithoutPlaylist() {
    }
}