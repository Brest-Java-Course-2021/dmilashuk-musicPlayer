package com.epam.brest.rest.daoImpl;


import com.epam.brest.dao.PlaylistDao;
import com.epam.brest.model.Playlist;
import com.epam.brest.model.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PlaylistDaoJdbc implements PlaylistDao, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistDaoJdbc.class);

    private NamedParameterJdbcTemplate template;

    @Override
    public void afterPropertiesSet() {
        if (template == null){
            LOGGER.error("NamedParameterJdbcTemplate was not injected");
            throw new BeanCreationException("NamedParameterJdbcTemplate is null on JdbcDepartmentDAO");}
    }

    @Autowired
    public PlaylistDaoJdbc(NamedParameterJdbcTemplate template, SongDaoJdbc songDaoJdbc) {
        this.template = template;
    }

    @Value("${sql.playlist.findAll}")
    private String sqlFindAll;

    @Value("${sql.playlist.findById}")
    private String sqlFindById;

    @Value("${sql.playlist.create}")
    private String sqlCreate;

    @Value("${sql.playlist.update}")
    private String sqlUpdate;

    @Value("${sql.playlist.delete}")
    private String sqlDelete;

    @Value("${sql.playlist.addSongIntoPlaylist}")
    private String sqlAddSongIntoPlaylist;

    @Value("${sql.playlist.removeSongFromPlaylist}")
    private String sqlRemoveSongFromPlaylist;

    @Value("${sql.playlist.findWithSongsById}")
    private String sqlFindWithSongsById;

    @Value("${sql.playlist.checkingUniquePlaylistSong}")
    private String sqlCheckingUniquePlaylistSong;

    @Value("${sql.playlist.checkingThatPlaylistNameUnique}")
    private String sqlCheckingThatPlaylistNameUnique;

    @Override
    public List<Playlist> findAll() {
        LOGGER.debug("PlaylistDaoJdbc: findAll()");
        return template.query(sqlFindAll, new PlaylistRowMapper());
    }

    @Override
    public Optional<Playlist> findById(Integer playlistId) {
        LOGGER.debug("PlaylistDaoJdbc: findById({})", playlistId);
        SqlParameterSource parameterSource = new MapSqlParameterSource("PLAYLIST_ID", playlistId);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(
                template.query(sqlFindById, parameterSource, new PlaylistRowMapper()))
        );
    }

    @Override
    public Integer create(Playlist playlist) {
        LOGGER.debug("PlaylistDaoJdbc: create({})", playlist);
        checkingThatPlaylistNameIsUnique(playlist);

        SqlParameterSource parameterSource = new MapSqlParameterSource("PLAYLIST_NAME", playlist.getPlaylistName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sqlCreate, parameterSource, keyHolder);

        Integer playlistId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        playlist.setPlaylistId(playlistId);
        return playlistId;
    }

    @Override
    public Integer update(Playlist playlist) {
        LOGGER.debug("PlaylistDaoJdbc: update({})", playlist);
        checkingThatPlaylistNameIsUnique(playlist);

        SqlParameterSource parameterSource = new MapSqlParameterSource("PLAYLIST_NAME", playlist.getPlaylistName())
                .addValue("PLAYLIST_ID", playlist.getPlaylistId());

        return template.update(sqlUpdate, parameterSource);
    }

    @Override
    public Integer delete(Integer playlistId) {
        LOGGER.debug("PlaylistDaoJdbc: delete({})", playlistId);
        return template.update(sqlDelete, new MapSqlParameterSource("PLAYLIST_ID", playlistId));
    }

    @Override
    public Optional<Playlist> findWithSongsById(Integer playlistId) {
        LOGGER.debug("PlaylistDaoJdbc: findWithSongsById({})", playlistId);
        SqlParameterSource parameterSource = new MapSqlParameterSource("PLAYLIST_ID", playlistId);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(
                template.query(sqlFindWithSongsById, parameterSource, new PlaylistResultSetExtractor()))
        );
    }

    @Override
    public Integer addSongIntoPlaylistById(Integer playlistId, Integer songId) {
        LOGGER.debug("PlaylistDaoJdbc: addSongIntoPlaylistById({},{})", playlistId, songId);

        checkingThatPlaylistAndSongExist(playlistId, songId);
        checkingThatSongUniqueInPlaylist(playlistId, songId);

        SqlParameterSource parameterSource = new MapSqlParameterSource("PLAYLIST_ID", playlistId)
                .addValue("SONG_ID", songId);

        return template.update(sqlAddSongIntoPlaylist, parameterSource);
    }

    @Override
    public Integer removeSongFromPlaylistById(Integer playlistId, Integer songId) {
        LOGGER.debug("PlaylistDaoJdbc: removeSongFromPlaylistById({},{})", playlistId, songId);
        SqlParameterSource parameterSource = new MapSqlParameterSource("PLAYLIST_ID", playlistId)
                .addValue("SONG_ID", songId);
        return template.update(sqlRemoveSongFromPlaylist, parameterSource);
    }

    private void checkingThatPlaylistNameIsUnique(Playlist playlist){

        boolean checkingThatPlaylistNameIsUnique = template.queryForObject(sqlCheckingThatPlaylistNameUnique,
                new MapSqlParameterSource("PLAYLIST_NAME",
                        playlist.getPlaylistName()), Integer.class) == 0;

        if (!checkingThatPlaylistNameIsUnique){
            LOGGER.warn("The same playlist is already exist {}", playlist);
            throw new IllegalArgumentException("Playlist is already exist");
        };
    }

    private void checkingThatPlaylistAndSongExist(Integer playlistId, Integer song_id){

        if (this.findById(playlistId).isEmpty()){
            LOGGER.warn("Playlist with this id={} does not exist",playlistId);
            throw new IllegalArgumentException("Playlist does not exist");
        }
        if(this.findById(song_id).isEmpty()){
            LOGGER.warn("Song with this id={} does not exist",song_id);
            throw new IllegalArgumentException("Song does not exist");
        }
    }

    private void checkingThatSongUniqueInPlaylist(Integer playlistId, Integer songId){
        SqlParameterSource parameterSource = new MapSqlParameterSource("PLAYLIST_ID", playlistId)
                .addValue("SONG_ID", songId);

        if(!(template.queryForObject(sqlCheckingUniquePlaylistSong, parameterSource, Integer.class) == 0)){
            LOGGER.warn("There is the same song({}) in this playlist({})", songId, playlistId);
            throw new  IllegalArgumentException("There is the same song in this playlist");
        };
    }

    private static class PlaylistRowMapper implements RowMapper<Playlist>{
        @Override
        public Playlist mapRow(ResultSet resultSet, int i) throws SQLException {
            Playlist playlist = new Playlist();
            playlist.setPlaylistId(resultSet.getInt("PLAYLIST_ID"));
            playlist.setPlaylistName(resultSet.getString("PLAYLIST_NAME"));
            return playlist;
        }
    }

    private static class PlaylistResultSetExtractor implements ResultSetExtractor<List<Playlist>>{
        @Override
        public List<Playlist> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Playlist> resultList = new ArrayList<>();
            Playlist playlist = null;
            while (resultSet.next()){
                if(playlist == null){
                    playlist = new Playlist();
                    playlist.setPlaylistId(resultSet.getInt("PLAYLIST_ID"));
                    playlist.setPlaylistName(resultSet.getString("PLAYLIST_NAME"));
                }
                Song song = new Song();
                song.setSongId(resultSet.getInt("SONG_ID"));
                song.setSinger(resultSet.getString("SINGER"));
                song.setTittle(resultSet.getString("TITTLE"));
                song.setAlbum(resultSet.getString("ALBUM"));
                song.setRealiseDate(resultSet.getDate("REALISE_DATE"));
                playlist.addSong(song);
            }
            resultList.add(playlist);
            return resultList;
        }
    }
}
