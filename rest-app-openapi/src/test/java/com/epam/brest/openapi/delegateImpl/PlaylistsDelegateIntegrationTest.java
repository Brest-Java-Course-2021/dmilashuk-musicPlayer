package com.epam.brest.openapi.delegateImpl;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;
import com.epam.brest.openapi.api.PlaylistsApiController;
import com.epam.brest.openapi.exception.CustomGlobalExceptionHandler;
import com.epam.brest.service.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
class PlaylistsDelegateIntegrationTest {
    private final Playlist playlist;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private PlaylistsDelegate playlistsDelegate;

    private MockMvc mockMvc;

    {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("New playlist");
        this.playlist = playlist;
    }

    @BeforeEach
    public void setup(){
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new PlaylistsApiController(playlistsDelegate))
                .setControllerAdvice(new CustomGlobalExceptionHandler())
                .build();
    }

    @Test
    void addSongIntoPlaylistTest() throws Exception {
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
    void removeSongFromPlaylistTest() throws Exception {
        Integer playlistId = 1;
        Integer songId = 2;
        when(playlistService.removeSongFromPlaylistById(1,2)).thenReturn(1);
        mockMvc.perform(delete("/playlists/{playlistId}/{songId}", playlistId, songId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(playlistService).removeSongFromPlaylistById(playlistId, songId);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    void createTest() throws Exception {
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

    @Test void deleteTest() throws Exception {
        when(playlistService.delete(anyInt())).thenReturn(1);
        Integer playlistId = 1;
        mockMvc.perform(delete("/playlists/{playlistId}", playlistId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(playlistService).delete(playlistId);
        verifyNoMoreInteractions(playlistService);
    }

    @Test
    void findAllTest() throws Exception {
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
    void findByIdTest() throws Exception {
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
    void findByIdWithSongsTest() throws Exception {
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
}