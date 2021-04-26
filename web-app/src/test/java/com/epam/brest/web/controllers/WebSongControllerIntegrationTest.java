package com.epam.brest.web.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.web.config.WebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({WebConfig.class})
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
    void findAllTest() throws Exception {
        Song song1 = createSong(1,"Singer1", "Song1");
        Song song2 = createSong(2,"Singer2", "Song2");

        List<Song> songList = Arrays.asList(song1, song2);

        mockServer.expect(once(), requestTo(new URI(songsUrl)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(songList))
                );

        mockMvc.perform(get("/songs")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("songs/songs"))
                .andExpect(model().attribute("songs", songList));
        mockServer.verify();
    }

    @Test
    void findAllWithDatesTest() throws Exception {
        Song song1 = createSong(1,"Singer1", "Song1");
        Song song2 = createSong(2,"Singer2", "Song2");

        List<Song> songList = Arrays.asList(song1, song2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(new GregorianCalendar(1990, Calendar.JANUARY,1).getTime());
        String endDate = dateFormat.format(new GregorianCalendar(1990, Calendar.JANUARY,1).getTime());
        mockServer.expect(once(), requestTo(new URI(songsUrl + "?startDate=" + startDate +  "&endDate=" + endDate)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(songList))
                );

        mockMvc.perform(get("/songs?startDate={startDate}}&endDate={endDate}", startDate, endDate)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("songs/songs"))
                .andExpect(model().attribute("songs", songList));
        mockServer.verify();
    }

    @Test
    void goToCreatePageTest() throws Exception {
        mockMvc.perform(get("/songs/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("songs/song"))
                .andExpect(model().attribute("isNew", true))
                .andExpect(model().attribute("method", "POST"))
                .andExpect(model().attribute("song", new Song()));
    }

    @Test
    void createTest() throws Exception {
        mockServer.expect(once(), requestTo(new URI(songsUrl)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("1")
                );

        mockMvc.perform(post("/songs")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("singer", "New singer"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/songs"))
                .andExpect(redirectedUrl("/songs"));
        mockServer.verify();
    }

    @Test
    void goToEditTest() throws Exception {
        Song song = createSong(1,"Singer1", "Song1");

        mockServer.expect(once(), requestTo(new URI(songsUrl + "/" + song.getSongId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(song))
                );

        mockMvc.perform(get("/songs/{songId}/edit", song.getSongId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("songs/song"))
                .andExpect(model().attribute("isNew", false))
                .andExpect(model().attribute("method", "PUT"))
                .andExpect(model().attribute("song", song));
        mockServer.verify();
    }

    @Test
    void editTest() throws Exception {
        mockServer.expect(once(), requestTo(new URI(songsUrl)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(put("/songs")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("singer", "New singer"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/songs"))
                .andExpect(redirectedUrl("/songs"));
        mockServer.verify();
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

    @Test
    void goToDeleteTest() throws Exception {
        Song song = createSong(1,"Bad singer", "Bad song");

        mockServer.expect(once(), requestTo(new URI(songsUrl + "/" + song.getSongId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(song))
                );

        mockMvc.perform(get("/songs/{songId}/delete", song.getSongId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("songs/songDelete"))
                .andExpect(model().attribute("song", song));
        mockServer.verify();
    }

    @Test
    void deleteTest() throws Exception {
        Integer songId = 1;
        mockServer.expect(once(), requestTo(new URI(songsUrl + "/" + songId)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(delete("/songs/{songId}", songId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/songs"))
                .andExpect(redirectedUrl("/songs"));
        mockServer.verify();
    }
}