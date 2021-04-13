package com.epam.brest.service.daoImpl;

import com.epam.brest.dao.SongDao;
import com.epam.brest.model.Song;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SongServiceDaoImplTest {

    @Mock
    private SongDao songDao;

    @InjectMocks
    private SongServiceDaoImpl songService;

    @Test
    void findAll() {
        List<Song> songList= Collections.singletonList(new Song());
        Mockito.when(songDao.findAll()).thenReturn(songList);
        List<Song> resultList = songService.findAll();

        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(songList, resultList);

        Mockito.verify(songDao).findAll();
        Mockito.verifyNoMoreInteractions(songDao);
    }

    @Test
    void findAllWithoutPlaylist() {
        List<Song> songList= Collections.singletonList(new Song());
        Integer playlistId = 1;
        Mockito.when(songDao.findAllWithoutPlaylist(playlistId)).thenReturn(songList);
        List<Song> resultList = songService.findAllWithoutPlaylist(playlistId);

        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(songList, resultList);

        Mockito.verify(songDao).findAllWithoutPlaylist(playlistId);
        Mockito.verifyNoMoreInteractions(songDao);
    }

    @Test
    void findAllByFilter() {
        List<Song> songList= Collections.singletonList(new Song());
        Date startDate = new Date(1000);
        Date endDate = new Date(2000);
        Mockito.when(songDao.findAllByFilter(startDate, endDate)).thenReturn(songList);
        List<Song> resultList = songService.findAllByFilter(startDate, endDate);

        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(songList, resultList);

        Mockito.verify(songDao).findAllByFilter(startDate, endDate);
        Mockito.verifyNoMoreInteractions(songDao);
    }

    @Test
    void findById() {
        Song song = new Song();
        song.setSinger("Some singer");
        Integer songId = 1;
        Mockito.when(songDao.findById(songId)).thenReturn(Optional.of(song));
        Optional<Song> result = songService.findById(songId);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(song, result.get());

        Mockito.verify(songDao).findById(songId);
        Mockito.verifyNoMoreInteractions(songDao);
    }

    @Test
    void create() {
        Song song = new Song();
        song.setSinger("Some singer");
        Integer songId = 6;
        Mockito.when(songDao.create(song)).thenReturn(songId);
        Integer resultSongId = songService.create(song);

        Assertions.assertNotNull(resultSongId);
        Assertions.assertEquals(songId, resultSongId);

        Mockito.verify(songDao).create(song);
        Mockito.verifyNoMoreInteractions(songDao);
    }

    @Test
    void update() {
        Song song = new Song();
        song.setSinger("Some singer");
        Integer responseValue = 1;
        Mockito.when(songDao.update(song)).thenReturn(responseValue);
        Integer result = songService.update(song);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(responseValue, result);

        Mockito.verify(songDao).update(song);
        Mockito.verifyNoMoreInteractions(songDao);
    }

    @Test
    void delete() {
        Integer songId = 5;
        Integer responseValue = 1;
        Mockito.when(songDao.delete(songId)).thenReturn(responseValue);
        Integer result = songService.delete(songId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(responseValue, result);

        Mockito.verify(songDao).delete(songId);
        Mockito.verifyNoMoreInteractions(songDao);
    }
}