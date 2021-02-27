package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.SongDao;
import com.epam.brest.model.Song;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SongDaoJdbc implements SongDao {

    @Override
    public List<Song> findAll() {
        return null;
    }

    @Override
    public List<Song> findByPlaylistId(Integer playlistId) {
        return null;
    }

    @Override
    public Optional<Song> findById(Integer songId) {
        return Optional.empty();
    }

    @Override
    public Integer create(Song song) {
        return null;
    }

    @Override
    public Integer update(Song song) {
        return null;
    }

    @Override
    public Integer delete(Song songId) {
        return null;
    }

}
