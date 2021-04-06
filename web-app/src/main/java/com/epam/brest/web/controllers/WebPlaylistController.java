package com.epam.brest.web.controllers;

import com.epam.brest.model.Playlist;
import com.epam.brest.web.serviceImpl.WebPlaylistServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

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
        model.addAttribute("playlists", playlistService.findAll());
        return "/playlists/playlists";
    }

    @GetMapping("/new")
    public final String goToCreateDepartmentPage(Model model) {
        model.addAttribute("tittle","Creat new playlist");
        model.addAttribute(new Playlist());
        model.addAttribute("method","POST");
        return "/playlists/playlist";
    }

    @PostMapping
    public final String create(@ModelAttribute Playlist department){
        playlistService.create(department);
        return "redirect:/playlists";
    }


}
