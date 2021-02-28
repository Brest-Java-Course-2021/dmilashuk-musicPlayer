package com.epam.brest.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlaylistTest {

    @Test
    public void addSongTest() {
        Playlist playlist = new Playlist();
        Song song1 = new Song();
        song1.setTittle("The show must go on");

        assertTrue(playlist.addSong(song1));
        assertEquals(song1,playlist.getSongs().get(0));
        assertFalse(playlist.addSong(song1));
        Song song2 = new Song();
        song2.setTittle("lord of the boards");
        assertTrue(playlist.addSong(song2));
        assertEquals(2,playlist.getSongs().size());
    }
}