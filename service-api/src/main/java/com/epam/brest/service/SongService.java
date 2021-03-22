package com.epam.brest.service;

import com.epam.brest.model.Song;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface SongService {
    /**
     * Get List with all existing songs
     * @return song list
     */
    List<Song> findAll();

    /**
     * Get a list with all songs that are not in the playlist
     * @param playlistId
     * @return song list
     */
    List<Song> findAllWithoutPlaylist(Integer playlistId);

    /**
     * Find all songs that realised between date1 and date2.
     * If passed only one date then return all songs by this release date
     * @param startDate - period start date
     * @param endDate - period end date
     * @return
     */
    List<Song> findAllByFilter(Date startDate, Date endDate);

    /**
     * Find a song by id
     * @param songId
     * @return Optional song
     */
    Optional<Song> findById(Integer songId);

    /**
     * Create a new song
     * @param song
     * @return Integer created song id
     */
    Integer create(Song song);

    /**
     * Update a song
     * @param song
     * @return Integer the number of rows affected
     */
    Integer update(Song song);

    /**
     * Delete a song
     * @param songId
     * @return Integer the number of rows affected
     */
    Integer delete(Integer songId);

}
