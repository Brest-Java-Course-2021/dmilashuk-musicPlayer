package com.epam.brest.kafka.model;

import com.epam.brest.model.Song;

public class SongEvent {

    private EventType eventType;
    private Song song;

    public SongEvent(EventType eventType, Song song) {
        this.eventType = eventType;
        this.song = song;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType=" + eventType +
                ", song=" + song +
                '}';
    }
}
