package com.epam.brest.model;

import java.util.Objects;

public class PlaylistDto {

    private Playlist playlist;

    private Double countOfSongs;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Double getCountOfSongs() {
        return countOfSongs;
    }

    public void setCountOfSongs(Double countOfSongs) {
        this.countOfSongs = countOfSongs;
    }

    @Override
    public String toString() {
        return "PlaylistDto{" +
                "playlist=" + playlist +
                ", countOfSongs=" + countOfSongs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaylistDto)) return false;
        PlaylistDto that = (PlaylistDto) o;
        return Objects.equals(playlist, that.playlist) &&
                Objects.equals(countOfSongs, that.countOfSongs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlist, countOfSongs);
    }
}
