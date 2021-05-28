package com.epam.brest.dao.jdbc.impl;

import com.epam.brest.dao.jdbc.config.TestJdbcDbConfig;
import com.epam.brest.model.Playlist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestJdbcDbConfig.class)
@Transactional
public class PlaylistDaoJdbcIntegrationTest {

    @Autowired
    private PlaylistDaoJdbc playlistDaoJdbc;

    @Autowired
    private SongDaoJdbc songDaoJdbc;

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final String SQL_CHECK_COUNT_PLAYLIST_SONG = "SELECT COUNT(PLAYLIST_ID) FROM PLAYLIST_SONG";

    @Test
    public void findAllTest(){
        assertEquals(2, playlistDaoJdbc.findAll().size());
    }

    @Test
    public void findByIdTest(){
        Optional<Playlist> result = playlistDaoJdbc.findById(2);
        assertTrue(result.isPresent());
        assertEquals(2, result.orElseThrow().getPlaylistId());
        assertTrue(playlistDaoJdbc.findById(999).isEmpty());
    }

    @Test
    public void createTest(){
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Good playlist");
        assertEquals(3, playlistDaoJdbc.create(playlist));
        Playlist playlist1 = new Playlist();
        playlist1.setPlaylistName("Old playlist");
        assertThrows(IllegalArgumentException.class, () -> playlistDaoJdbc.create(playlist1));
    }

    @Test
    public void updateTest(){
        Playlist playlist = new Playlist();
        playlist.setPlaylistName("Good playlist");
        playlist.setPlaylistId(1);
        assertEquals(1, playlistDaoJdbc.update(playlist));
        assertEquals("Good playlist", playlistDaoJdbc.findById(1).orElseThrow().getPlaylistName());
        playlist.setPlaylistId(999);
        playlist.setPlaylistName("Bad playlist");
        assertEquals(0, playlistDaoJdbc.update(playlist));
        Playlist playlist1 = new Playlist();
        playlist1.setPlaylistName("New playlist");
        playlist1.setPlaylistId(1);
        assertThrows(IllegalArgumentException.class, () -> playlistDaoJdbc.update(playlist1));
    }

    @Test
    public void deleteTest(){
        assertEquals(1, playlistDaoJdbc.delete(2));
        assertEquals(2, template.queryForObject(SQL_CHECK_COUNT_PLAYLIST_SONG, new MapSqlParameterSource(), Integer.class));
        assertEquals(5, songDaoJdbc.findAll().size());
    }

    @Test
    public void findWithSongsByIdTest(){
        Playlist playlist = playlistDaoJdbc.findWithSongsById(1).orElseThrow();
        assertNotNull(playlist);
        assertEquals("Old playlist", playlist.getPlaylistName());
        assertEquals(2, playlist.getSongs().size());
        System.out.println(playlist);
    }

    @Test
    public void addSongIntoPlaylistByIdTest(){
        assertEquals(1, playlistDaoJdbc.addSongIntoPlaylistById(1,2));
        assertEquals(3, playlistDaoJdbc.findWithSongsById(1).orElseThrow().getSongs().size());
        assertThrows(IllegalArgumentException.class, () -> playlistDaoJdbc.addSongIntoPlaylistById(999,1));
        assertThrows(IllegalArgumentException.class, () -> playlistDaoJdbc.addSongIntoPlaylistById(1,999));
        assertThrows(IllegalArgumentException.class, () -> playlistDaoJdbc.addSongIntoPlaylistById(1,2));
    }

    @Test
    public void removeSongFromPlaylistByIdTest(){
        assertEquals(1, playlistDaoJdbc.removeSongFromPlaylistById(1,3));
        assertEquals(1, playlistDaoJdbc.findWithSongsById(1).orElseThrow().getSongs().size());
    }
}