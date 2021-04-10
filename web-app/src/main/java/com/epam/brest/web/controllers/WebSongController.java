package com.epam.brest.web.controllers;

import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/songs")
public class WebSongController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSongController.class);

    private final SongService songService;

    public WebSongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/withoutPlaylist/{playlistId}")
    public String gotoAddSongIntoPlaylistPage(@PathVariable Integer playlistId,
                                              Model model){
        LOGGER.debug("gotoAddSongIntoPlaylistPage({})", playlistId);
        model.addAttribute("songs", songService.findAllWithoutPlaylist(playlistId));
        model.addAttribute("playlistId", playlistId);
        return "songs/addSongIntoPlaylist";
    }

}
