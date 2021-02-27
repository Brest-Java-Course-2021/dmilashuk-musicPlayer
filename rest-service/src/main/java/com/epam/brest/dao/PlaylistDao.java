package com.epam.brest.dao;

import com.epam.brest.model.Playlist;

import java.util.Map;
import java.util.Optional;

public interface PlaylistDao {

    Map<Playlist,Integer> findAll();
    Optional<Playlist> findById(Integer departmentId);
    Integer create(Playlist department);
    Integer update(Playlist department);
    Integer delete(Integer departmentId);

}
