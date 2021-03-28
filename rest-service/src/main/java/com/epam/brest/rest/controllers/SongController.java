package com.epam.brest.rest.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    //TODO to add validation of date
    @GetMapping
    public ResponseEntity<List<Song>> findAll(@RequestParam (name = "startDate", required = false) Date startDate,
                              @RequestParam (name = "endDate", required = false) Date endDate){
        if(startDate != null || endDate != null){
            LOGGER.debug("SongController: findAll({},{})", startDate, endDate);
            return new ResponseEntity<>(songService.findAllByFilter(startDate, endDate), HttpStatus.OK);
        }
        LOGGER.debug("SongController: findAll()");
        return new ResponseEntity<>(songService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/withoutPlaylist/{playlistId}")
    public ResponseEntity<List<Song>> findAllWithoutPlaylist(@PathVariable Integer playlistId){
        LOGGER.debug("SongController: findAllWithoutPlaylist({})", playlistId);
        return new ResponseEntity<> (songService.findAllWithoutPlaylist(playlistId), HttpStatus.OK);
    }

    @GetMapping("/{songId}")
    public ResponseEntity<Song> findById (@PathVariable Integer songId){
        LOGGER.debug("SongController: findById({})", songId);
        return new ResponseEntity<> (songService.findById(songId).orElseThrow
                (() -> new IllegalArgumentException("Song is not found"))
                ,HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Integer> create (@Valid @RequestBody Song song){
        LOGGER.debug("SongController: create({})", song);
        return new ResponseEntity<>(songService.create(song), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Integer> update (@Valid @RequestBody Song song){
        LOGGER.debug("SongController: update({})", song);
        return new ResponseEntity<>(songService.update(song), HttpStatus.OK);
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Integer> delete (@PathVariable Integer songId){
        LOGGER.debug("SongController: delete({})", songId);
        return new ResponseEntity<>(songService.delete(songId), HttpStatus.OK);
    }
}
