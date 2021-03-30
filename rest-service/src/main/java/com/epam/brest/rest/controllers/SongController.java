package com.epam.brest.rest.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Validated
@RequestMapping("/songs")
public class SongController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    //TODO add not found responses

    @GetMapping
    public ResponseEntity<List<Song>> findAll(@RequestParam (name = "startDate", required = false) String stringStartDate,
                                              @RequestParam (name = "endDate", required = false) String stringEndDate) throws ParseException {

        Date startDate = parsDate(stringStartDate);
        Date endDate = parsDate(stringEndDate);
        if(startDate != null || endDate != null){
            LOGGER.debug("SongController: findAll({},{})", startDate, endDate);
            return new ResponseEntity<>(songService.findAllByFilter(startDate, endDate), HttpStatus.OK);
        }
        LOGGER.debug("SongController: findAll()");
        return new ResponseEntity<>(songService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/withoutPlaylist/{playlistId}")
    public ResponseEntity<List<Song>> findAllWithoutPlaylist(@PathVariable
                                                                 @Positive(message = "Path variable should be positive")
                                                                         Integer playlistId){
        LOGGER.debug("SongController: findAllWithoutPlaylist({})", playlistId);
        return new ResponseEntity<> (songService.findAllWithoutPlaylist(playlistId), HttpStatus.OK);
    }

    @GetMapping("/{songId}")
    public ResponseEntity<Song> findById (@PathVariable
                                              @Positive(message = "Path variable should be positive")
                                                      Integer songId){
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
    public ResponseEntity<Integer> delete (@PathVariable
                                               @Positive(message = "Path variable should be positive")
                                                       Integer songId){
        LOGGER.debug("SongController: delete({})", songId);
        return new ResponseEntity<>(songService.delete(songId), HttpStatus.OK);
    }

    private Date parsDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(date);
    }
}
