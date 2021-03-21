package com.epam.brest.rest.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.rest.daoImpl.SongDaoJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SongController.class);

    private SongDaoJdbc songDaoJdbc;

    public SongController(SongDaoJdbc songDaoJdbc) {
        this.songDaoJdbc = songDaoJdbc;
    }

    @GetMapping
    public List<Song> findAll(@RequestParam (name = "startDate", required = false) Date startDate,
                              @RequestParam (name = "endDate", required = false) Date endDate){
        if(startDate != null || endDate != null){
            LOGGER.debug("SongController: findAll({},{})", startDate, endDate);
            List<Song> list = songDaoJdbc.findAllByFilter(startDate, endDate);
            System.out.println(list);
            return list;
        }
        LOGGER.debug("SongController: findAll()");
        return songDaoJdbc.findAll();
    }

    @GetMapping("/withoutPlaylist/{playlistId}")
    public List<Song> findAllWithoutPlaylist(@PathVariable Integer playlistId){
        LOGGER.debug("SongController: findAllWithoutPlaylist({})", playlistId);
        return songDaoJdbc.findAllWithoutPlaylist(playlistId);
    }

}
