package com.epam.brest.service.daoImpl;

import com.epam.brest.dao.PlaylistDao;
import com.epam.brest.model.Playlist;
import com.epam.brest.model.PlaylistDto;
import com.epam.brest.model.Song;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceDaoImplTest {

    @Mock
    private PlaylistDao playlistDao;

    @InjectMocks
    private PlaylistServiceDaoImpl playlistService;


    @Test
    void findAll() {
        List<PlaylistDto> playlistList= Collections.singletonList(new PlaylistDto());
        Mockito.when(playlistDao.findAll()).thenReturn(playlistList);
        List<PlaylistDto> resultList = playlistService.findAll();

        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(playlistList, resultList);

        Mockito.verify(playlistDao).findAll();
        Mockito.verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void findById() {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Some playlist");
        Integer playlistId = 1;
        Mockito.when(playlistDao.findById(playlistId)).thenReturn(Optional.of(playlist));
        Optional<Playlist> result = playlistService.findById(playlistId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(playlist, result.get());

        Mockito.verify(playlistDao).findById(playlistId);
        Mockito.verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void create() {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Some playlist");
        Integer songId = 6;
        Mockito.when(playlistDao.create(playlist)).thenReturn(songId);
        Integer resultSongId = playlistService.create(playlist);

        Assertions.assertNotNull(resultSongId);
        Assertions.assertEquals(songId, resultSongId);

        Mockito.verify(playlistDao).create(playlist);
        Mockito.verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void update() {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Some playlist");
        Integer responseValue = 1;
        Mockito.when(playlistDao.update(playlist)).thenReturn(responseValue);
        Integer result = playlistService.update(playlist);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(responseValue, result);

        Mockito.verify(playlistDao).update(playlist);
        Mockito.verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void delete() {
        Integer playlistId = 5;
        Integer responseValue = 1;
        Mockito.when(playlistDao.delete(playlistId)).thenReturn(responseValue);
        Integer result = playlistService.delete(playlistId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(responseValue, result);

        Mockito.verify(playlistDao).delete(playlistId);
        Mockito.verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void findWithSongsById() {
        Playlist playlistWithSongs = new Playlist();
        playlistWithSongs.setPlaylistName("Some playlist");
        Song song = new Song();
        song.setSinger("Some singer");
        playlistWithSongs.addSong(song);
        Integer playlistId = 1;
        Mockito.when(playlistDao.findWithSongsById(playlistId)).thenReturn(Optional.of(playlistWithSongs));
        Optional<Playlist> result = playlistService.findWithSongsById(playlistId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(playlistWithSongs,result.get());

        Mockito.verify(playlistDao).findWithSongsById(playlistId);
        Mockito.verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void addSongIntoPlaylistById() {
        Integer playlistId = 1;
        Integer songId = 100;
        Integer responseValue = 1;
        Mockito.when(playlistDao.addSongIntoPlaylistById(playlistId, songId)).thenReturn(responseValue);
        Integer result = playlistService.addSongIntoPlaylistById(playlistId, songId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(responseValue, result);

        Mockito.verify(playlistDao).addSongIntoPlaylistById(playlistId, songId);
        Mockito.verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void removeSongFromPlaylistById() {
        Integer playlistId = 1;
        Integer songId = 100;
        Integer responseValue = 1;
        Mockito.when(playlistDao.removeSongFromPlaylistById(playlistId, songId)).thenReturn(responseValue);
        Integer result = playlistService.removeSongFromPlaylistById(playlistId, songId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(responseValue, result);

        Mockito.verify(playlistDao).removeSongFromPlaylistById(playlistId, songId);
        Mockito.verifyNoMoreInteractions(playlistDao);
    }
}