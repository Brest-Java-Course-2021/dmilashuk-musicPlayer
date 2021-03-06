package com.epam.brest.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {

    @Positive(message = "Playlist id should be positive")
    private Integer playlistId;

    @NotBlank(message = "Playlist name should not be empty")
    @Size(min = 1, max = 30, message = "Playlist name should be between 1 and 30 characters")
    private String playlistName;

    private List<Song> songs;

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
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
                Objects.equals(playlistName, playlist.playlistName) &&
                Objects.equals(songs, playlist.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, playlistName, songs);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "playlistId=" + playlistId +
                ", playlistName='" + playlistName + '\'' +
                ", songs=" + songs +
                '}';
    }
}
