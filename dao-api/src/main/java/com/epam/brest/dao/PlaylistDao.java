package com.epam.brest.dao;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;

import java.util.List;
import java.util.Optional;

public interface PlaylistDao {

    /**
     * Find all existing playlists with count of songs
     * @return playlistDto list
     */
    List<PlaylistDto> findAll();

    /**
     * Find a playlist by id
     * @param playlistId
     * @return Optional playlist
     */
    Optional<Playlist> findById(Integer playlistId);

    /**
     * Create a new playlist
     * @param playlist
     * @return Integer created playlist id
     */
    Integer create(Playlist playlist);

    /**
     * Update a playlist
     * @param playlist
     * @return Integer the number of rows affected
     */
    Integer update(Playlist playlist);

    /**
     * Delete a playlist
     * @param playlist
     * @return Integer the number of rows affected
     */
    Integer delete(Integer playlist);

    /**
     * Find playlist by id with its songs
     * @param playlistId
     * @return Optional playlist
     */
    Optional<Playlist> findWithSongsById(Integer playlistId);

    /**
     * Add song to playlist by their ids
     * @param playlistId
     * @param songId
     * @return Integer the number of rows affected
     */
    Integer addSongIntoPlaylistById(Integer playlistId, Integer songId);
    /**
     * Remove song from playlist by their ids
     * @param playlistId
     * @param songId
     * @return Integer the number of rows affected
     */
    Integer removeSongFromPlaylistById(Integer playlistId, Integer songId);
}
