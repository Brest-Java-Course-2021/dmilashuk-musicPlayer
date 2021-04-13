package com.epam.brest.service.restImpl;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;
import com.epam.brest.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:restServer.properties")
public class WebPlaylistServiceImpl implements PlaylistService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebPlaylistServiceImpl.class);

    private final RestTemplate restTemplate;

    private String rootUrl;

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    public WebPlaylistServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        this.rootUrl = String.format("%s://%s:%d/playlists", protocol, host, port);
    }

    @Override
    public List<PlaylistDto> findAll() {
        LOGGER.debug("findAll()");

        ParameterizedTypeReference<List<PlaylistDto>> typeReference = new ParameterizedTypeReference<>(){};
        ResponseEntity<List<PlaylistDto>> responseEntity = restTemplate.exchange(rootUrl, HttpMethod.GET,null, typeReference);
        return responseEntity.getBody();
    }

    @Override
    public Optional<Playlist> findById(Integer playlistId) {
        LOGGER.debug("findById({})", playlistId);

        ResponseEntity<Playlist> responseEntity = restTemplate.getForEntity(rootUrl + "/" + playlistId, Playlist.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer create(Playlist playlist) {
        LOGGER.debug("create({})", playlist);

        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(rootUrl, playlist, Integer.class);
        return responseEntity.getBody();
    }

    @Override
    public Integer update(Playlist playlist) {
        LOGGER.debug("update({})", playlist);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Playlist> entity = new HttpEntity<>(playlist, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(rootUrl, HttpMethod.PUT, entity, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful() ? 1 : 0;
    }

    @Override
    public Integer delete(Integer playlistId) {
        LOGGER.debug("delete({})", playlistId);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(rootUrl + "/" + playlistId, HttpMethod.DELETE, null, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful() ? 1 : 0;
    }

    @Override
    public Optional<Playlist> findWithSongsById(Integer playlistId) {
        LOGGER.debug("findWithSongsById({})", playlistId);

        ResponseEntity<Playlist> responseEntity = restTemplate.getForEntity(rootUrl + "/" + playlistId + "/withSongs", Playlist.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer addSongIntoPlaylistById(Integer playlistId, Integer songId) {
        LOGGER.debug("addSongIntoPlaylistById({},{})", playlistId, songId);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(rootUrl + "/" + playlistId + "/" + songId, HttpMethod.POST, null, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful() ? 1 : 0;
    }

    @Override
    public Integer removeSongFromPlaylistById(Integer playlistId, Integer songId) {
        LOGGER.debug("removeSongFromPlaylistById({},{})", playlistId, songId);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(rootUrl + "/" + playlistId + "/" + songId, HttpMethod.DELETE, null, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful() ? 1 : 0;
    }
}
