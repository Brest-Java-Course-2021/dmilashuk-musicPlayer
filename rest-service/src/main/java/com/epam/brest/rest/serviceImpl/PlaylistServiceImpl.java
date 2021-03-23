package com.epam.brest.rest.serviceImpl;

import com.epam.brest.dao.PlaylistDao;
import com.epam.brest.model.Playlist;
import com.epam.brest.service.PlaylistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistServiceImpl.class);

    private final PlaylistDao playlistDao;

    public PlaylistServiceImpl(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    @Override
    public List<Playlist> findAll() {
        LOGGER.debug("PlaylistServiceImpl: findAll()");
        return playlistDao.findAll();
    }

    @Override
    public Optional<Playlist> findById(Integer playlistId) {
        LOGGER.debug("PlaylistServiceImpl: findById({})", playlistId);
        return playlistDao.findById(playlistId);
    }

    @Override
    public Integer create(Playlist playlist) {
        LOGGER.debug("PlaylistServiceImpl: create({})", playlist);
        return playlistDao.create(playlist);
    }

    @Override
    public Integer update(Playlist playlist) {
        LOGGER.debug("PlaylistServiceImpl: update({})", playlist);
        return playlistDao.update(playlist);
    }

    @Override
    public Integer delete(Integer playlist) {
        LOGGER.debug("PlaylistServiceImpl: delete({})", playlist);
        return playlistDao.delete(playlist);
    }

    @Override
    public Optional<Playlist> findWithSongsById(Integer playlistId) {
        LOGGER.debug("PlaylistServiceImpl: findWithSongsById({})", playlistId);
        return playlistDao.findWithSongsById(playlistId);
    }

    @Override
    public Integer addSongIntoPlaylistById(Integer playlistId, Integer songId) {
        LOGGER.debug("PlaylistServiceImpl: addSongIntoPlaylistById({},{})", playlistId, songId);
        return playlistDao.addSongIntoPlaylistById(playlistId, songId);
    }

    @Override
    public Integer removeSongFromPlaylistById(Integer playlistId, Integer songId) {
        LOGGER.debug("PlaylistServiceImpl: removeSongFromPlaylistById({}, {})", playlistId, songId);
        return playlistDao.removeSongFromPlaylistById(playlistId, songId);
    }
}
