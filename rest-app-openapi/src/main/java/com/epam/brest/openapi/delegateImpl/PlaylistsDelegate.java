package com.epam.brest.openapi.delegateImpl;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;
import com.epam.brest.openapi.api.PlaylistsApiDelegate;
import com.epam.brest.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class PlaylistsDelegate implements PlaylistsApiDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistsDelegate.class);

    private final PlaylistService playlistService;

    public PlaylistsDelegate(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Override
    public ResponseEntity<Object> addSongIntoPlaylistUsingPOST(Integer playlistId, Integer songId) {
        LOGGER.debug("addSongIntoPlaylistUsingPOST({}, {})", playlistId, songId);
        return playlistService.addSongIntoPlaylistById(playlistId, songId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<Integer> createUsingPOST(Playlist playlist) {
        LOGGER.debug("createUsingPOST({})", playlist);
        return new ResponseEntity<>(playlistService.create(playlist), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteUsingDELETE(Integer playlistId) {
        LOGGER.debug("deleteUsingDELETE ({})", playlistId);
        return playlistService.delete(playlistId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<PlaylistDto>> findAllUsingGET() {
        LOGGER.debug("findAllUsingGET()");
        List<PlaylistDto> resultList = playlistService.findAll();
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Playlist> findByIdUsingGET(Integer playlistId) {
        LOGGER.debug("findByIdUsingGET({})", playlistId);
        Optional<Playlist> result = playlistService.findById(playlistId);
        return result.map(playlist -> new ResponseEntity<>(playlist, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Playlist> findByIdWithSongsUsingGET(Integer playlistId) {
        LOGGER.debug("findByIdWithSongsUsingGET({})", playlistId);
        Optional<Playlist> result = playlistService.findWithSongsById(playlistId);
        return result.map(playlist -> new ResponseEntity<>(playlist, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Object> removeSongFromPlaylistUsingDELETE(Integer playlistId, Integer songId) {
        LOGGER.debug("removeSongFromPlaylistUsingDELETE({}, {})", playlistId, songId);
        return playlistService.removeSongFromPlaylistById(playlistId, songId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<Object> updateUsingPUT(Playlist playlist) {
        LOGGER.debug("updateUsingPUT({})", playlist);
        return playlistService.update(playlist) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
