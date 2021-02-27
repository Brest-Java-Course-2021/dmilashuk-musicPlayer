package com.epam.brest.model;

import java.util.Objects;

public class Playlist {

    private Integer playlistId;

    private String tittle;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(playlistId, playlist.playlistId) &&
                Objects.equals(tittle, playlist.tittle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, tittle);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "playlistId=" + playlistId +
                ", tittle='" + tittle + '\'' +
                '}';
    }
}
