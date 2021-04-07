package com.epam.brest.web.controllers;

import com.epam.brest.model.Playlist;
import com.epam.brest.web.serviceImpl.WebPlaylistServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "/playlists")
public class WebPlaylistController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebPlaylistController.class);

    private final WebPlaylistServiceImpl playlistService;

    public WebPlaylistController(WebPlaylistServiceImpl playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping()
    public String findAll(Model model){
        LOGGER.debug("findAll()");
        model.addAttribute("playlistsDto", playlistService.findAll());
        return "playlists/playlists";
    }

    @GetMapping("/new")
    public String goToCreatePage(Model model) {
        LOGGER.debug("goToCreateDepartmentPage()");
        model.addAttribute("isNew",true);
        model.addAttribute("method","POST");
        model.addAttribute(new Playlist());
        return "playlists/playlist";
    }

    @PostMapping
    public String create(Playlist playlist){
        LOGGER.debug("create({})", playlist);
        playlistService.create(playlist);
        return "redirect:/playlists";
    }

    @GetMapping("/{playlistId}/edit")
    public String goToEditPage(@PathVariable Integer playlistId,
                               Model model){
        LOGGER.debug("goToEditPage(playlistId = {})", playlistId);
        Optional<Playlist> result = playlistService.findById(playlistId);
        if (result.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("playlist",result.get());
            return "playlists/playlist";
        }else {
            LOGGER.warn("Playlist {} not found", playlistId);
            model.addAttribute("errorMessage", "Playlist " + playlistId + " not found");
            model.addAttribute("backTo", "/departments");
            return "errorPage";
        }
    }



}
