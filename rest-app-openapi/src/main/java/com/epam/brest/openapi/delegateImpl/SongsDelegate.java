package com.epam.brest.openapi.delegateImpl;

import com.epam.brest.model.Song;
import com.epam.brest.openapi.api.SongsApiDelegate;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class SongsDelegate implements SongsApiDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SongsDelegate.class);

    private final SongService songService;

    public SongsDelegate(SongService songService) {
        this.songService = songService;
    }

    @Override
    public ResponseEntity<Integer> createUsingPOST1(Song song) {
        LOGGER.debug("createUsingPOST1({})", song);
        return new ResponseEntity<>(songService.create(song), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteUsingDELETE1(Integer songId) {
        LOGGER.debug("deleteUsingDELETE1({})", songId);
        return songService.delete(songId) != 0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Song>> findAllUsingGET1(LocalDate endDate, LocalDate startDate) {
        if (startDate != null || endDate != null) {
            LOGGER.debug("findAllUsingGET1({},{})", endDate, startDate);
            Date verifiedStartDate = (startDate == null) ? null : Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date verifiedEndDate = (endDate == null) ? null : Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            List<Song> result = songService.findAllByFilter(verifiedStartDate, verifiedEndDate);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        LOGGER.debug("findAllUsingGET1()");
        List<Song> result = songService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Song>> findAllWithoutPlaylistUsingGET(Integer playlistId) {
        LOGGER.debug("findAllWithoutPlaylistUsingGET({})", playlistId);
        List<Song> resultList = songService.findAllWithoutPlaylist(playlistId);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Song> findByIdUsingGET1(Integer songId) {
        LOGGER.debug("findByIdUsingGET1({})", songId);
        Optional<Song> result = songService.findById(songId);
        return result.map(song -> new ResponseEntity<>(song, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Object> updateUsingPUT1(Song song) {
        LOGGER.debug("updateUsingPUT1({})", song);
        return songService.update(song) !=0
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
