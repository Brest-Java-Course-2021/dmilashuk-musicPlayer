INSERT INTO PLAYLIST (PLAYLIST_NAME) VALUES ('Old playlist'),
                                            ('New playlist');

INSERT INTO SONG (SINGER, TITTLE, ALBUM, REALISE_DATE) VALUES ('Metallica','Nothing else matters','Metallica','1992-03-12'),
                                                              ('Michael Jackson','Billie jean','Thriller','1983-01-02'),
                                                              ('Queen','The show must go on','Innuendo','1991-10-14'),
                                                              ('Metallica','Enter Sandman','Metallica','1991-06-29'),
                                                              ('Queen','Another One Bites the Dust','The Game','1980-08-22');

INSERT INTO PLAYLIST_SONG (PLAYLIST_ID, SONG_ID) VALUES (1,3),
                                                        (1,5),
                                                        (2,1),
                                                        (2,4);
