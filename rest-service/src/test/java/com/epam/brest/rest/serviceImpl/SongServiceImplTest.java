package com.epam.brest.rest.serviceImpl;

import com.epam.brest.dao.SongDao;
import com.epam.brest.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceImplTest {

    @Mock
    private SongDao songDao;

    @InjectMocks
    private SongServiceImpl songService;

    @Test
    void findAll() {
        List<Song> songList= Collections.singletonList(new Song());
        when(songDao.findAll()).thenReturn(songList);
        List<Song> resultList = songService.findAll();

        assertNotNull(resultList);
        assertEquals(songList, resultList);

        verify(songDao).findAll();
        verifyNoMoreInteractions(songDao);
    }

    @Test
    void findAllWithoutPlaylist() {
        List<Song> songList= Collections.singletonList(new Song());
        Integer playlistId = 1;
        when(songDao.findAllWithoutPlaylist(playlistId)).thenReturn(songList);
        List<Song> resultList = songService.findAllWithoutPlaylist(playlistId);

        assertNotNull(resultList);
        assertEquals(songList, resultList);

        verify(songDao).findAllWithoutPlaylist(playlistId);
        verifyNoMoreInteractions(songDao);
    }

    @Test
    void findAllByFilter() {
        List<Song> songList= Collections.singletonList(new Song());
        Date startDate = new Date(1000);
        Date endDate = new Date(2000);
        when(songDao.findAllByFilter(startDate, endDate)).thenReturn(songList);
        List<Song> resultList = songService.findAllByFilter(startDate, endDate);

        assertNotNull(resultList);
        assertEquals(songList, resultList);

        verify(songDao).findAllByFilter(startDate, endDate);
        verifyNoMoreInteractions(songDao);
    }

    @Test
    void findById() {
        Song song = new Song();
        song.setSinger("Some singer");
        Integer songId = 1;
        when(songDao.findById(songId)).thenReturn(Optional.of(song));
        Optional<Song> result = songService.findById(songId);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(song, result.get());

        verify(songDao).findById(songId);
        verifyNoMoreInteractions(songDao);
    }

    @Test
    void create() {
        Song song = new Song();
        song.setSinger("Some singer");
        Integer songId = 6;
        when(songDao.create(song)).thenReturn(songId);
        Integer resultSongId = songService.create(song);

        assertNotNull(resultSongId);
        assertEquals(songId, resultSongId);

        verify(songDao).create(song);
        verifyNoMoreInteractions(songDao);
    }

    @Test
    void update() {
        Song song = new Song();
        song.setSinger("Some singer");
        Integer responseValue = 1;
        when(songDao.update(song)).thenReturn(responseValue);
        Integer result = songService.update(song);

        assertNotNull(result);
        assertEquals(responseValue, result);

        verify(songDao).update(song);
        verifyNoMoreInteractions(songDao);
    }

    @Test
    void delete() {
        Integer songId = 5;
        Integer responseValue = 1;
        when(songDao.delete(songId)).thenReturn(responseValue);
        Integer result = songService.delete(songId);

        assertNotNull(result);
        assertEquals(responseValue, result);

        verify(songDao).delete(songId);
        verifyNoMoreInteractions(songDao);
    }
}