package com.epam.brest.rest.controllers;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;
import com.epam.brest.rest.controllers.exception.CustomGlobalExceptionHandler;
import com.epam.brest.service.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RestPlaylistControllerIntegrationTest {

    private final Playlist playlist;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PlaylistService playlistService;

    private MockMvc mockMvc;

    {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("New playlist");
        this.playlist = playlist;
    }

    @BeforeEach
    public void setup(){
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new RestPlaylistController(playlistService, new LoggingMeterRegistry() {
                }))
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();
    }

    @Test
    public void findAllTest() throws Exception {
        List<PlaylistDto> listDto = Collections.singletonList(new PlaylistDto());
        when(playlistService.findAll()).thenReturn(listDto);

        String responseBody = mockMvc.perform(get("/playlists"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PlaylistDto> responseList = objectMapper.readValue(
                responseBody,
                objectMapper.getTypeFactory().constructCollectionType(List.class, PlaylistDto.class));

        assertEquals(listDto, responseList);

        verify(playlistService).findAll();
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void findByIdTest() throws Exception {
        Integer playlistId = 1;
        when(playlistService.findById(playlistId)).thenReturn(Optional.ofNullable(this.playlist));
        String responseBody = mockMvc.perform(get("/playlists/{playlistId}", playlistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Playlist responseSong = objectMapper.readValue(responseBody, Playlist.class);

        assertEquals(this.playlist, responseSong);
        verify(playlistService).findById(playlistId);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void findByIdNotFoundTest () throws Exception {
        Integer playlistId = 99;
        when(playlistService.findById(playlistId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/playlists/{playlistId}", playlistId))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(playlistService).findById(playlistId);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void createTest() throws Exception {
        when(playlistService.create(any(Playlist.class))).thenReturn(1);
        String requestBody = objectMapper.writeValueAsString(this.playlist);

        mockMvc.perform(post("/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));
        verify(playlistService).create(this.playlist);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void createNotBlankTest() throws Exception {
        Playlist errorPlaylist = new Playlist();
        String requestBody = objectMapper.writeValueAsString(errorPlaylist);
        errorPlaylist.setPlaylistName("    ");

        mockMvc.perform(post("/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0]").value("Playlist name should not be empty"));

        verifyNoInteractions(playlistService);
    }

    @Test
    public void createBadSizeTest() throws Exception {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(RandomStringUtils.randomAlphabetic(31));
        String requestBody = objectMapper.writeValueAsString(playlist);

        mockMvc.perform(post("/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors[0]").value("Playlist name should be between 1 and 30 characters"));

        verifyNoInteractions(playlistService);
    }

    @Test
    public void createThrowIllegalArgumentExceptionTest() throws Exception {
        String errorMessage = "Some message";
        when(playlistService.create(any(Playlist.class))).thenThrow(new IllegalArgumentException(errorMessage));
        String requestBody = objectMapper.writeValueAsString(this.playlist);
        mockMvc.perform(post("/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").value(errorMessage));

        verify(playlistService).create(this.playlist);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void updateTest() throws Exception {
        when(playlistService.update(any(Playlist.class))).thenReturn(1);
        String requestBody = objectMapper.writeValueAsString(this.playlist);
        mockMvc.perform(put("/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk());

        verify(playlistService).update(this.playlist);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void deleteTest() throws Exception {
        when(playlistService.delete(anyInt())).thenReturn(1);
        Integer playlistId = 1;
        mockMvc.perform(delete("/playlists/{playlistId}", playlistId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(playlistService).delete(playlistId);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void findByIdWithSongsTest() throws Exception {
        Integer playlistId = 1;
        when(playlistService.findWithSongsById(playlistId)).thenReturn(Optional.ofNullable(this.playlist));
        String responseBody = mockMvc.perform(get("/playlists/{playlistId}/withSongs", playlistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Playlist responseSong = objectMapper.readValue(responseBody, Playlist.class);

        assertEquals(this.playlist, responseSong);
        verify(playlistService).findWithSongsById(playlistId);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void addSongIntoPlaylistTest () throws Exception {
        Integer playlistId = 1;
        Integer songId = 2;
        when(playlistService.addSongIntoPlaylistById(1,2)).thenReturn(1);
        mockMvc.perform(post("/playlists/{playlistId}/{songId}", playlistId, songId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(playlistService).addSongIntoPlaylistById(playlistId, songId);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    public void removeSongFromPlaylist () throws Exception {
        Integer playlistId = 1;
        Integer songId = 2;
        when(playlistService.removeSongFromPlaylistById(1,2)).thenReturn(1);
        mockMvc.perform(delete("/playlists/{playlistId}/{songId}", playlistId, songId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(playlistService).removeSongFromPlaylistById(playlistId, songId);
        verifyNoMoreInteractions(playlistService);
    }


}
