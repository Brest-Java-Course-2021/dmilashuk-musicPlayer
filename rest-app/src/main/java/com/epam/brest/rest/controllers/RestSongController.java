package com.epam.brest.rest.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/songs")
public class RestSongController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestSongController.class);

    private final SongService songService;

    public RestSongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> findAll(@RequestParam(name = "startDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                      Date startDate,
                                              @RequestParam(name = "endDate", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                      Date endDate) {
        if(startDate != null || endDate != null){
            LOGGER.debug("RestSongController: findAll({},{})", startDate, endDate);
            List<Song> result = songService.findAllByFilter(startDate, endDate);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        LOGGER.debug("RestSongController: findAll()");
        List<Song> result = songService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/withoutPlaylist/{playlistId}")
    public ResponseEntity<List<Song>> findAllWithoutPlaylist(@PathVariable
                                                                 @Positive(message = "Path variable should be positive")
                                                                         Integer playlistId){
        LOGGER.debug("RestSongController: findAllWithoutPlaylist({})", playlistId);
        List<Song> resultList = songService.findAllWithoutPlaylist(playlistId);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping(value = "/{songId}")
    public ResponseEntity<Song> findById (@PathVariable
                                              @Positive(message = "Path variable should be positive")
                                                      Integer songId){
        LOGGER.debug("RestSongController: findById({})", songId);
        Optional<Song> result = songService.findById(songId);
        return result.map(song -> new ResponseEntity<>(song, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Integer> create (@Valid @RequestBody Song song){
        LOGGER.debug("RestSongController: create({})", song);
        return new ResponseEntity<>(songService.create(song), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> update (@Valid @RequestBody Song song){
        LOGGER.debug("RestSongController: update({})", song);
        return songService.update(song) !=0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Object> delete (@PathVariable
                                               @Positive(message = "Path variable should be positive")
                                                       Integer songId){
        LOGGER.debug("RestSongController: delete({})", songId);
        return songService.delete(songId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
