package com.epam.brest.web.controllers;

import com.epam.brest.model.Song;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/songs")
public class WebSongController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSongController.class);

    private final SongService songService;

    public WebSongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public String findAll(@RequestParam(name = "startDate", required = false)
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                  Date startDate,
                          @RequestParam(name = "endDate", required = false)
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                  Date endDate,
                          Model model) {
        List<Song> resultList;
        if(startDate != null || endDate != null){
            LOGGER.debug("findAll({},{})", startDate, endDate);
            resultList = songService.findAllByFilter(startDate, endDate);
        }else {
            LOGGER.debug("findAll()");
            resultList = songService.findAll();
        }
        model.addAttribute("songs", resultList);
        return "songs/songs";
    }

    @GetMapping("/new")
    public String goToCreatePage(Model model) {
        LOGGER.debug(" goToCreatePage()");
        model.addAttribute("isNew",true);
        model.addAttribute("method","POST");
        model.addAttribute(new Song());
        return "songs/song";
    }

    @PostMapping
    public String create(@ModelAttribute Song song){
        LOGGER.debug("create({})", song);
        songService.create(song);
        return "redirect:/songs";
    }

    @GetMapping("/{songId}/edit")
    public String goToEditPage(@PathVariable Integer songId,
                               Model model){
        LOGGER.debug("goToEditPage(playlistId = {})", songId);
        Optional<Song> playlistOptional = songService.findById(songId);
        model.addAttribute("method", "PUT");
        model.addAttribute("isNew", false);
        model.addAttribute("song", playlistOptional.orElseThrow());
        return "songs/song";
    }

    @PutMapping
    public String edit(@ModelAttribute Song song){
        LOGGER.debug(" edit({})", song);
        songService.update(song);
        return "redirect:/songs";
    }

    @GetMapping("/{songId}/delete")
    public String goToDeletePage(@PathVariable Integer songId,
                                 Model model){
        LOGGER.debug("goToDeletePage({})", songId);
        Optional<Song> playlistOptional = songService.findById(songId);
        model.addAttribute("song", playlistOptional.orElseThrow());
        return "songs/songDelete";
    }

    @DeleteMapping("/{songId}")
    public String delete (@PathVariable Integer songId){
        LOGGER.debug("delete({})",songId);
        songService.delete(songId);
        return "redirect:/songs";
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
