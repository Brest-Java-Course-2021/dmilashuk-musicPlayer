package com.epam.brest.dao;
import com.epam.brest.model.Song;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface SongDao {

    List<Song> findAll();
    List<Song> findByFilter(String singer);
    List<Song> findByFilter(Date date1, Date date2);
    List<Song> findByFilter(String singer, Date date1, Date date2);
    Optional<Song> findById(Integer songId);
    Integer create(Song song);
    Integer update(Song song);
    Integer delete(Song songId);

}
