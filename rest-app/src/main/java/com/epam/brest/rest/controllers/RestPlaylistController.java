package com.epam.brest.rest.controllers;

import com.epam.brest.model.Playlist;
import com.epam.brest.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Controller
@Validated
@RequestMapping("/playlists")
public class RestPlaylistController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestSongController.class);

    private final PlaylistService playlistService;

    public RestPlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<List<Playlist>> findAll(){
        LOGGER.debug("RestPlaylistController: findAll()");
        List<Playlist> resultList = playlistService.findAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> findById(@PathVariable
                                                 @Positive(message = "Path variable should be positive")
                                                         Integer playlistId){
        LOGGER.debug("RestPlaylistController: findById({})", playlistId);
        Optional<Playlist> result = playlistService.findById(playlistId);
        return result.map(playlist -> new ResponseEntity<>(playlist, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Integer> create(@Valid @RequestBody Playlist playlist){
        LOGGER.debug("RestPlaylistController: create({})", playlist);
        return new ResponseEntity<>(playlistService.create(playlist), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> update (@Valid @RequestBody Playlist playlist){
        LOGGER.debug("RestPlaylistController: update({})", playlist);
        return playlistService.update(playlist) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Object> delete (@PathVariable
                                          @Positive(message = "Path variable should be positive")
                                                  Integer playlistId){
        LOGGER.debug("RestPlaylistController: delete({})", playlistId);
        return playlistService.delete(playlistId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{playlistId}/withSongs")
    public ResponseEntity<Playlist> findByIdWithSongs(@PathVariable
                                                                 @Positive(message = "Path variable should be positive")
                                                                         Integer playlistId){
        LOGGER.debug("RestPlaylistController: findWithSongsById({})", playlistId);
        Optional<Playlist> result = playlistService.findWithSongsById(playlistId);
        return result.map(playlist -> new ResponseEntity<>(playlist, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{playlistId}/{songId}")
    public ResponseEntity<Object> addSongIntoPlaylist(@PathVariable
                                                      @Positive(message = "Path variable should be positive")
                                                              Integer playlistId,
                                                      @PathVariable
                                                      @Positive(message = "Path variable should be positive")
                                                              Integer songId) {
        LOGGER.debug("RestPlaylistController: addSongIntoPlaylist({}, {})", playlistId, songId);
        return playlistService.addSongIntoPlaylistById(playlistId, songId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{playlistId}/{songId}")
    public ResponseEntity<Object> removeSongFromPlaylist(@PathVariable
                                                         @Positive(message = "Path variable should be positive")
                                                                 Integer playlistId,
                                                         @PathVariable
                                                         @Positive(message = "Path variable should be positive")
                                                                 Integer songId) {
        LOGGER.debug("RestPlaylistController: removeSongFromPlaylist({}, {})", playlistId, songId);
        return playlistService.removeSongFromPlaylistById(playlistId, songId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
