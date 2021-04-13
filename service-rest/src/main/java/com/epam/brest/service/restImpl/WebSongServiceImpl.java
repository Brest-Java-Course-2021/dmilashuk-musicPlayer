package com.epam.brest.service.restImpl;

import com.epam.brest.model.Song;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:restServer.properties")
public class WebSongServiceImpl implements SongService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSongServiceImpl.class);

    private final RestTemplate restTemplate;

    private String rootUrl;

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    public WebSongServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        this.rootUrl = String.format("%s://%s:%d/songs", protocol, host, port);
    }

    @Override
    public List<Song> findAll() {
        LOGGER.debug("findAll()");

        ParameterizedTypeReference<List<Song>> typeReference = new ParameterizedTypeReference<>(){};
        ResponseEntity<List<Song>> responseEntity = restTemplate.exchange(rootUrl, HttpMethod.GET,null, typeReference);
        return responseEntity.getBody();
    }

    @Override
    public List<Song> findAllWithoutPlaylist(Integer playlistId) {
        LOGGER.debug("findAllWithoutPlaylist({})", playlistId);

        ParameterizedTypeReference<List<Song>> typeReference = new ParameterizedTypeReference<>(){};
        ResponseEntity<List<Song>> responseEntity = restTemplate.exchange(rootUrl + "/withoutPlaylist/" + playlistId, HttpMethod.GET,null, typeReference);
        return responseEntity.getBody();
    }

    @Override
    public List<Song> findAllByFilter(Date startDate, Date endDate) {
        LOGGER.debug("findAllByFilter({},{})", startDate, endDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String stringStartDate = null;
        String stringEndDate = null;
        if(startDate != null) stringStartDate = dateFormat.format(startDate);
        if(endDate!= null) stringEndDate = dateFormat.format(endDate);
        ParameterizedTypeReference<List<Song>> typeReference = new ParameterizedTypeReference<>(){};
        ResponseEntity<List<Song>> responseEntity = restTemplate.exchange(rootUrl + "?startDate={startDate}&endDate={endDate}",
                HttpMethod.GET,null, typeReference,  stringStartDate, stringEndDate);
        return responseEntity.getBody();
    }

    @Override
    public Optional<Song> findById(Integer songId) {
        LOGGER.debug("findById({})", songId);

        ResponseEntity<Song> responseEntity = restTemplate.getForEntity(rootUrl + "/" + songId, Song.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Integer create(Song song) {
        LOGGER.debug("create({})", song);

        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(rootUrl, song, Integer.class);
        return responseEntity.getBody();
    }

    @Override
    public Integer update(Song song) {
        LOGGER.debug("update({})", song);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Song> entity = new HttpEntity<>(song, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(rootUrl, HttpMethod.PUT, entity, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful() ? 1 : 0;
    }

    @Override
    public Integer delete(Integer songId) {
        LOGGER.debug("delete({})", songId);

        ResponseEntity<Void> responseEntity = restTemplate.exchange(rootUrl + "/" + songId, HttpMethod.DELETE, null, Void.class);
        return responseEntity.getStatusCode().is2xxSuccessful() ? 1 : 0;
    }

}
