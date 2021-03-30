package com.epam.brest.rest.serviceImpl;

import com.epam.brest.dao.SongDao;
import com.epam.brest.model.Song;
import com.epam.brest.service.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SongServiceImpl implements SongService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SongServiceImpl.class);

    private final SongDao songDao;

    public SongServiceImpl(SongDao songDao) {
        this.songDao = songDao;
    }

    @Override
    public List<Song> findAll() {
        LOGGER.debug("SongServiceImpl: findAll()");
        return songDao.findAll();
    }

    @Override
    public List<Song> findAllWithoutPlaylist(Integer playlistId) {
        LOGGER.debug("SongServiceImpl: findAllWithoutPlaylist({})", playlistId);
        return songDao.findAllWithoutPlaylist(playlistId);
    }

    @Override
    public List<Song> findAllByFilter(Date startDate, Date endDate) {
        LOGGER.debug("SongServiceImpl: findAllByFilter({}, {})", startDate, endDate);
        return songDao.findAllByFilter(startDate, endDate);
    }

    @Override
    public Optional<Song> findById(Integer songId) {
        LOGGER.debug("SongServiceImpl: findById({})", songId);
        return songDao.findById(songId);
    }

    @Override
    public Integer create(Song song) {
        LOGGER.debug("SongServiceImpl: create({})", song);
        return songDao.create(song);
    }

    @Override
    public Integer update(Song song) {
        LOGGER.debug("SongServiceImpl: update({})", song);
        return songDao.update(song);
    }

    @Override
    public Integer delete(Integer songId) {
        LOGGER.debug("SongServiceImpl: delete({})", songId);
        return songDao.delete(songId);
    }
}
