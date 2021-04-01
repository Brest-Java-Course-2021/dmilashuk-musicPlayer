package com.epam.brest.rest.serviceImpl;

import com.epam.brest.dao.PlaylistDao;
import com.epam.brest.model.Playlist;
import com.epam.brest.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestPlaylistServiceImplTest {

    @Mock
    private PlaylistDao playlistDao;

    @InjectMocks
    private RestPlaylistServiceImpl playlistService;


    @Test
    void findAll() {
        List<Playlist> playlistList= Collections.singletonList(new Playlist());
        when(playlistDao.findAll()).thenReturn(playlistList);
        List<Playlist> resultList = playlistService.findAll();

        assertNotNull(resultList);
        assertEquals(playlistList, resultList);

        verify(playlistDao).findAll();
        verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void findById() {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Some playlist");
        Integer playlistId = 1;
        when(playlistDao.findById(playlistId)).thenReturn(Optional.of(playlist));
        Optional<Playlist> result = playlistService.findById(playlistId);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(playlist, result.get());

        verify(playlistDao).findById(playlistId);
        verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void create() {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Some playlist");
        Integer songId = 6;
        when(playlistDao.create(playlist)).thenReturn(songId);
        Integer resultSongId = playlistService.create(playlist);

        assertNotNull(resultSongId);
        assertEquals(songId, resultSongId);

        verify(playlistDao).create(playlist);
        verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void update() {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Some playlist");
        Integer responseValue = 1;
        when(playlistDao.update(playlist)).thenReturn(responseValue);
        Integer result = playlistService.update(playlist);

        assertNotNull(result);
        assertEquals(responseValue, result);

        verify(playlistDao).update(playlist);
        verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void delete() {
        Integer playlistId = 5;
        Integer responseValue = 1;
        when(playlistDao.delete(playlistId)).thenReturn(responseValue);
        Integer result = playlistService.delete(playlistId);

        assertNotNull(result);
        assertEquals(responseValue, result);

        verify(playlistDao).delete(playlistId);
        verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void findWithSongsById() {
        Playlist playlistWithSongs = new Playlist();
        playlistWithSongs.setPlaylistName("Some playlist");
        Song song = new Song();
        song.setSinger("Some singer");
        playlistWithSongs.addSong(song);
        Integer playlistId = 1;
        when(playlistDao.findWithSongsById(playlistId)).thenReturn(Optional.of(playlistWithSongs));
        Optional<Playlist> result = playlistService.findWithSongsById(playlistId);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(playlistWithSongs,result.get());

        verify(playlistDao).findWithSongsById(playlistId);
        verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void addSongIntoPlaylistById() {
        Integer playlistId = 1;
        Integer songId = 100;
        Integer responseValue = 1;
        when(playlistDao.addSongIntoPlaylistById(playlistId, songId)).thenReturn(responseValue);
        Integer result = playlistService.addSongIntoPlaylistById(playlistId, songId);

        assertNotNull(result);
        assertEquals(responseValue, result);

        verify(playlistDao).addSongIntoPlaylistById(playlistId, songId);
        verifyNoMoreInteractions(playlistDao);
    }

    @Test
    void removeSongFromPlaylistById() {
        Integer playlistId = 1;
        Integer songId = 100;
        Integer responseValue = 1;
        when(playlistDao.removeSongFromPlaylistById(playlistId, songId)).thenReturn(responseValue);
        Integer result = playlistService.removeSongFromPlaylistById(playlistId, songId);

        assertNotNull(result);
        assertEquals(responseValue, result);

        verify(playlistDao).removeSongFromPlaylistById(playlistId, songId);
        verifyNoMoreInteractions(playlistDao);
    }
}