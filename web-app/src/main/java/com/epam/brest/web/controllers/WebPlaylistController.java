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
        Optional<Playlist> playlistOptional = playlistService.findById(playlistId);
            model.addAttribute("method", "PUT");
            model.addAttribute("isNew", false);
            model.addAttribute("playlist", playlistOptional.orElseThrow());
            return "playlists/playlist";
    }

    @PutMapping
    public String edit(Playlist playlist){
        LOGGER.debug(" edit({})", playlist);
        playlistService.update(playlist);
        return "redirect:/playlists";
    }

    @GetMapping("/{playlistId}/delete")
    public String goToDeletePage(@PathVariable Integer playlistId,
                                 Model model){
        LOGGER.debug("goToDeletePage({})", playlistId);
        Optional<Playlist> playlistOptional = playlistService.findById(playlistId);
        model.addAttribute("playlist", playlistOptional.orElseThrow());
        return "playlists/playlistDelete";
    }

    @DeleteMapping("/{playlistId}")
    public String delete (@PathVariable Integer playlistId){
        LOGGER.debug("delete({})",playlistId);
        playlistService.delete(playlistId);
        return "redirect:/playlists";
    }

    @GetMapping("/{playlistId}")
    public String goToPlaylistPage(@PathVariable Integer playlistId,
                                   Model model){
        LOGGER.debug("goToPlaylistPage({})", playlistId);
        Optional<Playlist> playlistOptional = playlistService.findWithSongsById(playlistId);
        model.addAttribute("playlist", playlistOptional.orElseThrow());
        return "playlists/changePlaylist";
    }

    @PostMapping("/{playlistId}/{songId}")
    public String addSongIntoPlaylist(@PathVariable Integer playlistId, @PathVariable Integer songId){
        LOGGER.debug("addSongIntoPlaylist({},{})", playlistId, songId);
        playlistService.addSongIntoPlaylistById(playlistId, songId);
        String redirectUrl = "/playlists/" + playlistId;
        return "redirect:" + redirectUrl;
    }

    @DeleteMapping("/{playlistId}/{songId}")
    public String removeSongFromPlaylist(@PathVariable Integer playlistId, @PathVariable Integer songId){
        LOGGER.debug("removeSongFromPlaylist({},{})", playlistId, songId);
        playlistService.removeSongFromPlaylistById(playlistId, songId);
        String redirectUrl = "/playlists/" + playlistId;
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/{playlistId}/addSong")
    public String goToAddSongPage (@PathVariable Integer playlistId){
        LOGGER.debug("goToAddSongPage({})", playlistId);
        return "playlists/changePlaylist";
    }

}
