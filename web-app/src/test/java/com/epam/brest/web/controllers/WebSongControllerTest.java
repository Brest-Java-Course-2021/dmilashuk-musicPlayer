package com.epam.brest.web.controllers;

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
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebAppConfiguration
@ContextConfiguration(classes = WebConfig.class)
class WebSongControllerIntegrationTest implements InitializingBean {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    private String songsUrl;

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
        this.songsUrl = String.format("%s://%s:%d/songs", protocol, host, port);
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void gotoAddSongIntoPlaylistPageTest() throws Exception {

        Integer playlistId = 1;

        Song song1 = createSong(1,"Singer1", "Song1");
        Song song2 = createSong(2,"Singer2", "Song2");

        List<Song> songList = Arrays.asList(song1, song2);

        mockServer.expect(once(), requestTo(new URI(songsUrl + "/withoutPlaylist/" + playlistId)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(songList))
                );

        mockMvc.perform(get("/songs/withoutPlaylist/{playlistId}", playlistId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("songs/addSongIntoPlaylist"))
                .andExpect(model().attribute("songs", songList));
        mockServer.verify();
    }

    private Song createSong (Integer songId, String singer, String tittle){
        Song song = new Song();
        song.setSongId(songId);
        song.setSinger(singer);
        song.setTittle(tittle);
        return song;
    }
}