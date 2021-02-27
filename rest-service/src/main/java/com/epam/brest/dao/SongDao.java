package com.epam.brest.dao;
import com.epam.brest.model.Song;

import java.util.List;
import java.util.Optional;

public interface SongDao {

    List<Song> findAll();
    List<Song> findByPlaylistId(Integer playlistId);
    Optional<Song> findById(Integer songId);
    Integer create(Song song);
    Integer update(Song song);
    Integer delete(Song songId);

}
