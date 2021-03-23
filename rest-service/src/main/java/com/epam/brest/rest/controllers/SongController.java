package com.epam.brest.rest.controllers;

import com.epam.brest.dao.SongDao;
import com.epam.brest.model.Song;
import com.epam.brest.rest.daoImpl.SongDaoJdbc;
import com.epam.brest.rest.serviceImpl.SongServiceImpl;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<Song> findAll(@RequestParam (name = "startDate", required = false) Date startDate,
                              @RequestParam (name = "endDate", required = false) Date endDate){
        if(startDate != null || endDate != null){
            LOGGER.debug("SongController: findAll({},{})", startDate, endDate);
            return songService.findAllByFilter(startDate, endDate);
        }
        LOGGER.debug("SongController: findAll()");
        return songService.findAll();
    }

    @GetMapping("/withoutPlaylist/{playlistId}")
    public List<Song> findAllWithoutPlaylist(@PathVariable Integer playlistId){
        LOGGER.debug("SongController: findAllWithoutPlaylist({})", playlistId);
        return songService.findAllWithoutPlaylist(playlistId);
    }

}
