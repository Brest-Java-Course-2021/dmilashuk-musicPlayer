package com.epam.brest.dao;

import com.epam.brest.model.Playlist;
import com.epam.brest.model.Song;

import java.util.Map;
import java.util.Optional;

public interface PlaylistDao {

    Map<Playlist,Integer> findAll();
    Optional<Playlist> findById(Integer playlistId);
    Integer create(Playlist playlist);
    Integer update(Playlist playlist);
    Integer delete(Integer playlist);
    Optional<Playlist> findWithSongsById(Integer playlistId);
    Integer addSongById(Integer playlistId, Song song);
}
