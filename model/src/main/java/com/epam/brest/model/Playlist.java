package com.epam.brest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {

    private Integer playlistId;

    private String tittle;

    private List<Song> songs;

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public boolean addSong(Song song){
        if (songs == null){
            songs = new ArrayList<>();
            songs.add(song);
            return true;
        }else if (songs.contains(song)){
            return false;
        }
        songs.add(song);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(playlistId, playlist.playlistId) &&
                Objects.equals(tittle, playlist.tittle) &&
                Objects.equals(songs, playlist.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, tittle, songs);
    }
}
