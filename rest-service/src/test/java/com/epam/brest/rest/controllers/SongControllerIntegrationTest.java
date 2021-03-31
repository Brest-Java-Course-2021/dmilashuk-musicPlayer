package com.epam.brest.rest.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.rest.config.RestRootConfig;
import com.epam.brest.rest.config.RestWebConfig;
import com.epam.brest.rest.controllers.exception.CustomGlobalExceptionHandler;
import com.epam.brest.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebAppConfiguration
@ContextConfiguration(classes = {RestWebConfig.class, RestRootConfig.class})
class SongControllerIntegrationTest {

    private final List<Song> list;

    private final Song song;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private SongService songService;

    private MockMvc mockMvc;

    {
        Song song1 = new Song();
        song1.setSinger("Singer1");
        song1.setTittle("Tittle");
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
                .standaloneSetup(new SongController(songService))
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();
    }

    @Test
    public void findAllWithoutRequestParamTest() throws Exception {
        when(this.songService.findAll()).thenReturn(list);
        String responseBody = mockMvc.perform(get("/songs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList);
        verify(songService).findAll();
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void findAllWithRequestParamTest() throws Exception {
        when(this.songService.findAllByFilter(any(Date.class), any(Date.class))).thenReturn(list);
        String responseBody = mockMvc.perform(get("/songs")
                .param("startDate", "1992-03-12")
                .param("endDate", "2000-03-12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList);
        verify(songService).findAllByFilter(any(Date.class), any(Date.class));
        verifyNoMoreInteractions(songService);

    }

    @Test
    public void findAllWithOnlyStartDateTest() throws Exception {
        when(this.songService.findAllByFilter(any(Date.class), isNull())).thenReturn(list);
        String responseBody1 = mockMvc.perform(get("/songs")
                .param("startDate", "1992-03-12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList1 = objectMapper.readValue(
                responseBody1,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList1);
        verify(songService).findAllByFilter(any(Date.class), isNull());
        verifyNoMoreInteractions(songService);
    }


    @Test
    public void findAllWithOnlyEndDateTest() throws Exception {
        when(this.songService.findAllByFilter(isNull(), any(Date.class))).thenReturn(list);
        String responseBody2 = mockMvc.perform(get("/songs")
                .param("endDate", "1992-03-12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList2 = objectMapper.readValue(
                responseBody2,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class)
        );

        assertEquals(this.list, responseList2);
        verify(songService).findAllByFilter(isNull(), any(Date.class));
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void findAllWithoutPlaylistTest() throws Exception {
        when(songService.findAllWithoutPlaylist(anyInt())).thenReturn(list);
        Integer playlistId = 1;
        String responseBody = mockMvc.perform(get("/songs/withoutPlaylist/{playlistId}", playlistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        List<Song> responseList = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Song.class));
        assertEquals(this.list, responseList);

        verify(songService).findAllWithoutPlaylist(playlistId);
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void findByIdTest () throws Exception {
        Integer songId = 1;
        when(songService.findById(songId)).thenReturn(Optional.ofNullable(song));
        String responseBody = mockMvc.perform(get("/songs/{songId}", songId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Song responseSong = objectMapper.readValue(responseBody, Song.class);

        assertEquals(this.song, responseSong);
        verify(songService).findById(songId);
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void findByIdNotFoundTest () throws Exception {
        Integer songId = 99;
        when(songService.findById(songId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/songs/{songId}", songId))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").value("Song is not found"));
        verify(songService).findById(songId);
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void createTest() throws Exception {
        when(songService.create(any(Song.class))).thenReturn(1);
        String requestBody = objectMapper.writeValueAsString(this.song);

        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));
        verify(songService).create(this.song);
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void createNotBlankTest() throws Exception {
        Song errorSong = new Song();
        String requestBody = objectMapper.writeValueAsString(errorSong);
        errorSong.setTittle("    ");

        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0]").value("Singer should not be empty"))
                .andExpect(jsonPath("$.errors[1]").value("Tittle should not be empty"));

        verifyNoInteractions(songService);
    }

    @Test
    public void createBadSizeTest() throws Exception {
        Song errorSong = new Song();
        errorSong.setSinger(RandomStringUtils.randomAlphabetic(31));
        errorSong.setTittle(RandomStringUtils.randomAlphabetic(61));
        errorSong.setAlbum(RandomStringUtils.randomAlphabetic(31));
        String requestBody = objectMapper.writeValueAsString(errorSong);


        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0]").value("Album should be between 1 and 60 characters"))
                .andExpect(jsonPath("$.errors[1]").value("Singer should be between 1 and 30 characters"))
                .andExpect(jsonPath("$.errors[2]").value("Tittle should be between 1 and 60 characters"));

        verifyNoInteractions(songService);
    }

    @Test
    public void createThrowIllegalArgumentExceptionTest() throws Exception {
        String errorMessage = "Some message";
        when(songService.create(any(Song.class))).thenThrow(new IllegalArgumentException(errorMessage));
        String requestBody = objectMapper.writeValueAsString(this.song);
        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").value(errorMessage));

        verify(songService).create(this.song);
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void updateTest() throws Exception {
        when(songService.update(any(Song.class))).thenReturn(1);
        String requestBody = objectMapper.writeValueAsString(song);
        mockMvc.perform(put("/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));

        verify(songService).update(song);
        verifyNoMoreInteractions(songService);
    }

    @Test
    public void deleteTest() throws Exception {
        when(songService.delete(anyInt())).thenReturn(1);
        Integer songId = 1;
        mockMvc.perform(delete("/songs/{songId}", songId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));

        verify(songService).delete(songId);
        verifyNoMoreInteractions(songService);
    }

    //TODO add test for id and date validation
}