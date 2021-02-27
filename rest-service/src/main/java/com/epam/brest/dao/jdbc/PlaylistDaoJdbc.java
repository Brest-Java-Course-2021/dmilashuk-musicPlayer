package com.epam.brest.dao.jdbc;

import com.epam.brest.dao.PlaylistDao;
import com.epam.brest.model.Playlist;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class PlaylistDaoJdbc implements PlaylistDao {

    @Override
    public Map<Playlist, Integer> findAll() {
        return null;
    }

    @Override
    public Optional<Playlist> findById(Integer departmentId) {
        return Optional.empty();
    }

    @Override
    public Integer create(Playlist department) {
        return null;
    }

    @Override
    public Integer update(Playlist department) {
        return null;
    }

    @Override
    public Integer delete(Integer departmentId) {
        return null;
    }
}
