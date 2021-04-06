package com.epam.brest.web.controllers;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;
import com.epam.brest.model.Song;
import com.epam.brest.web.config.WebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
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
    public void afterPropertiesSet() throws Exception {
        this.playlistsUrl = String.format("%s://%s:%d/playlists", protocol, host, port);
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        this.mockServer = MockRestServiceServer.createServer(restTemplate);;
    }

    @Test
    void findAllTest() throws Exception {
        Playlist playlist1 = createPlaylist(1,"First playlist",null);
        Playlist playlist2 = createPlaylist(2,"Second playlist", null);
        Playlist playlist3 = createPlaylist(3,"Third playlist", null);

        PlaylistDto playlistDto1 = createPlaylistDto(playlist1, 5);
        PlaylistDto playlistDto2 = createPlaylistDto(playlist2, 10);
        PlaylistDto playlistDto3 = createPlaylistDto(playlist3, 0);

        List<PlaylistDto> songList = Arrays.asList(playlistDto1, playlistDto2, playlistDto3);

        mockServer.expect(once(), requestTo(new URI(playlistsUrl)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(songList))
                );

        mockMvc.perform(MockMvcRequestBuilders.get("/playlists"))
/*                .andExpect(content().contentType("text/html;charset=UTF-8"))*/
                .andExpect(status().isOk())
                .andExpect(view().name("/playlists/playlists"))
                .andExpect(model().attribute("playlistsDto", songList));

        mockServer.verify();
    }

    @Test
    void goToCreateDepartmentPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/playlists/new"))
/*                .andExpect(content().contentType("text/html;charset=UTF-8"))*/
                .andExpect(status().isOk())
                .andExpect(view().name("/playlists/playlist"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("method", "POST"))
                .andExpect(model().attribute("playlist", new Playlist()));
    }

    @Test
    void create() {
    }

    private Playlist createPlaylist (Integer playlistId, String playlistName,@Nullable List<Song> songList){
        Playlist playlist = new Playlist();
        playlist.setPlaylistId(playlistId);
        playlist.setPlaylistName(playlistName);
        if(songList != null){
            playlist.setSongs(songList);
        }
        return playlist;
    }

    private PlaylistDto createPlaylistDto (Playlist playlist, Integer countOfSongs){
        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setPlaylist(playlist);
        playlistDto.setCountOfSongs(countOfSongs);
        return playlistDto;
    }
}