package com.epam.brest.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

public class Song {

    @Positive(message = "Playlist id should be positive")
    private Integer songId;

    @NotBlank(message = "Singer should not be empty")
    @Size(min = 1, max = 30, message = "Singer should be between 1 and 30 characters")
    private String singer;

    @NotBlank(message = "Tittle should not be empty")
    @Size(min = 1, max = 60, message = "Tittle should be between 1 and 60 characters")
    private String tittle;

    @Size(max = 30, message = "Album should not be greater than 30 characters")
    private String album;

    @PastOrPresent(message = "Realise date should be past or present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date realiseDate;

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Date getRealiseDate() {
        return realiseDate;
    }

    public void setRealiseDate(Date realiseDate) {
        this.realiseDate = realiseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return Objects.equals(songId, song.songId) &&
                Objects.equals(singer, song.singer) &&
                Objects.equals(tittle, song.tittle) &&
                Objects.equals(album, song.album) &&
                Objects.equals(realiseDate, song.realiseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, singer, tittle, album, realiseDate);
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", singer='" + singer + '\'' +
                ", tittle='" + tittle + '\'' +
                ", album='" + album + '\'' +
                ", realiseDate=" + realiseDate +
                '}';
    }
}
