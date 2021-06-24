
CREATE TABLE IF NOT EXISTS PLAYLIST
(
    PLAYLIST_ID SERIAL,
    PLAYLIST_NAME VARCHAR(30) NOT NULL UNIQUE,
    PRIMARY KEY (PLAYLIST_ID)
);

CREATE TABLE IF NOT EXISTS SONG
(
    SONG_ID SERIAL,
    SINGER VARCHAR(30) NOT NULL,
    TITTLE VARCHAR(60) NOT NULL,
    ALBUM VARCHAR(30),
    REALISE_DATE DATE,
    PRIMARY KEY (SONG_ID),
    CONSTRAINT Unique_Names UNIQUE (SINGER,TITTLE)
);

CREATE TABLE IF NOT EXISTS PLAYLIST_SONG
(
    PLAYLIST_ID INT NOT NULL,
    SONG_ID INT NOT NULL,
    PRIMARY KEY (PLAYLIST_ID, SONG_ID),
    CONSTRAINT FK_PLAYLIST FOREIGN KEY (PLAYLIST_ID) REFERENCES PLAYLIST (PLAYLIST_ID) ON DELETE CASCADE,
    CONSTRAINT FK_SONG FOREIGN KEY (SONG_ID) REFERENCES SONG (SONG_ID) ON DELETE CASCADE
);