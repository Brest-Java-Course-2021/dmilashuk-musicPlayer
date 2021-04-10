package com.epam.brest.web.controllers;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;
import com.epam.brest.model.Song;
import com.epam.brest.web.config.WebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
class WebPlaylistControllerIntegrationTest implements InitializingBean {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    private String playlistsUrl;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;

    @Override
    public void afterPropertiesSet() {
        this.playlistsUrl = String.format("%s://%s:%d/playlists", protocol, host, port);
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void findAllTest() throws Exception {
        Playlist playlist1 = createPlaylist(1,"First playlist",null);
        Playlist playlist2 = createPlaylist(2,"Second playlist", null);
        Playlist playlist3 = createPlaylist(3,"Third playlist", null);

        PlaylistDto playlistDto1 = createPlaylistDto(playlist1, 5);
        PlaylistDto playlistDto2 = createPlaylistDto(playlist2, 10);
        PlaylistDto playlistDto3 = createPlaylistDto(playlist3, 0);

        List<PlaylistDto> dtoList = Arrays.asList(playlistDto1, playlistDto2, playlistDto3);

        mockServer.expect(once(), requestTo(new URI(playlistsUrl)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(dtoList))
                );

        mockMvc.perform(get("/playlists")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("playlists/playlists"))
                .andExpect(model().attribute("playlistsDto", dtoList));
        mockServer.verify();
    }

    @Test
    void goToCreatePageTest() throws Exception {
        mockMvc.perform(get("/playlists/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("playlists/playlist"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("method", "POST"))
                .andExpect(model().attribute("playlist", new Playlist()));
    }

    @Test
    void createTest() throws Exception {
        mockServer.expect(once(), requestTo(new URI(playlistsUrl)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        mockMvc.perform(post("/playlists")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playlistName", "My new playlist"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/playlists"))
                .andExpect(redirectedUrl("/playlists"));
        mockServer.verify();
    }

    @Test
    void goToEditTest() throws Exception {
        Playlist playlist = createPlaylist(1,"Bad playlist", null);

        mockServer.expect(once(), requestTo(new URI(playlistsUrl + "/" + playlist.getPlaylistId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(playlist))
                );

        mockMvc.perform(get("/playlists/{playlistId}/edit", playlist.getPlaylistId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("playlists/playlist"))
                .andExpect(model().attribute("isNew", false))
                .andExpect(model().attribute("method", "PUT"))
                .andExpect(model().attribute("playlist", playlist));
        mockServer.verify();
    }

    @Test
    void updateTest() throws Exception {
        mockServer.expect(once(), requestTo(new URI(playlistsUrl)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(put("/playlists")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playlistName", "My new playlist"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/playlists"))
                .andExpect(redirectedUrl("/playlists"));
        mockServer.verify();
    }

    @Test
    void goToDeleteTest() throws Exception {
        Playlist playlist = createPlaylist(1,"Bad playlist", null);

        mockServer.expect(once(), requestTo(new URI(playlistsUrl + "/" + playlist.getPlaylistId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(playlist))
                );

        mockMvc.perform(get("/playlists/{playlistId}/delete", playlist.getPlaylistId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("playlists/playlistDelete"))
                .andExpect(model().attribute("playlist", playlist));
        mockServer.verify();
    }

    @Test
    void deleteTest() throws Exception {
        Integer playlistId = 1;
        mockServer.expect(once(), requestTo(new URI(playlistsUrl + "/" + playlistId)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(delete("/playlists/{playlistId}", playlistId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/playlists"))
                .andExpect(redirectedUrl("/playlists"));
        mockServer.verify();
    }

    @Test
    void goToPlaylistTest() throws Exception {
        Song song1 = createSong(1,"Singer1", "Song1");
        Song song2 = createSong(2,"Singer2", "Song2");
        Playlist playlist = createPlaylist(1,"Bad playlist", Arrays.asList(song1, song2));

        mockServer.expect(once(), requestTo(new URI(playlistsUrl + "/" + playlist.getPlaylistId() + "/withSongs")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(playlist))
                );

        mockMvc.perform(get("/playlists/{playlistId}", playlist.getPlaylistId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("playlists/changePlaylist"))
                .andExpect(model().attribute("playlist", playlist));
        mockServer.verify();
    }

    @Test
    void addSongIntoPlaylistTest() throws Exception{
        Integer playlistId = 1;
        Integer songId = 2;

        mockServer.expect(once(), requestTo(new URI(playlistsUrl + "/" + playlistId + "/" + songId)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(post("/playlists/{playlistId}/{songId}", playlistId, songId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/playlists/" + playlistId))
                .andExpect(redirectedUrl("/playlists/" + playlistId));
        mockServer.verify();
    }

    @Test
    void removeSongFromPlaylistTest() throws Exception{
        Integer playlistId = 1;
        Integer songId = 2;

        mockServer.expect(once(), requestTo(new URI(playlistsUrl + "/" + playlistId + "/" + songId)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(delete("/playlists/{playlistId}/{songId}", playlistId, songId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/playlists/" + playlistId))
                .andExpect(redirectedUrl("/playlists/" + playlistId));
        mockServer.verify();
    }



    private Playlist createPlaylist (@Nullable Integer playlistId, String playlistName,@Nullable List<Song> songList){
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);
        playlist.setPlaylistId(playlistId);
        playlist.setSongs(songList);
        return playlist;
    }

    private PlaylistDto createPlaylistDto (Playlist playlist, Integer countOfSongs){
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setPlaylist(playlist);
        playlistDto.setCountOfSongs(countOfSongs);
        return playlistDto;
    }

    private Song createSong (Integer songId, String singer, String tittle){
        Song song = new Song();
        song.setSongId(songId);
        song.setSinger(singer);
        song.setTittle(tittle);
        return song;
    }
}