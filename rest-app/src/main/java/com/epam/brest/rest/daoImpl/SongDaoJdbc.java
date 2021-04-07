package com.epam.brest.rest.daoImpl;

import com.epam.brest.dao.SongDao;
import com.epam.brest.model.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class SongDaoJdbc implements SongDao, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(SongDaoJdbc.class);

    private NamedParameterJdbcTemplate template;

    private final RowMapper<Song> songRowMapper = BeanPropertyRowMapper.newInstance(Song.class);

    @Value("${sql.song.findAllSongs}")
    private String sqlFindAllSongs;

    @Value("${sql.song.findAllSongWithoutPlaylist}")
    private String sqlFindAllSongWithoutPlaylist;

    @Value("${sql.song.findAllSongsByFilter}")
    private String sqlFindAllSongsByFilter;

    @Value("${sql.song.findById}")
    private String sqlFindById;

    @Value("${sql.song.create}")
    private String sqlCreate;

    @Value("${sql.song.update}")
    private String sqlUpdate;

    @Value("${sql.song.delete}")
    private String sqlDelete;

    @Value("${sql.song.checkSongUnique}")
    private String sqlCheckSongUnique;

    @Autowired
    public void setTemplate(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (template == null){
            LOGGER.error("NamedParameterJdbcTemplate was not injected");
            throw new BeanCreationException("NamedParameterJdbcTemplate is null on JdbcDepartmentDAO");}
    }

    @Override
    public List<Song> findAll() {
        LOGGER.debug("SongDaoJdbc: findAll()");
        return template.query(sqlFindAllSongs, songRowMapper);
    }

    @Override
    public List<Song> findAllWithoutPlaylist(Integer playlistId) {
        LOGGER.debug("SongDaoJdbc: findAllWithoutPlaylist({})",playlistId);
        return template.query(sqlFindAllSongWithoutPlaylist,new MapSqlParameterSource("PLAYLIST_ID", playlistId), songRowMapper);
    }

    @Override
    public List<Song> findAllByFilter(Date startDate, Date endDate) {
        LOGGER.debug("SongDaoJdbc: findAllByFilter({}, {})", startDate, endDate);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(startDate == null){
            parameterSource.addValue("START_DATE", endDate);
            parameterSource.addValue("END_DATE", endDate);
        }else if (endDate == null){
            parameterSource.addValue("START_DATE", startDate);
            parameterSource.addValue("END_DATE", startDate);
        }else {
            parameterSource.addValue("START_DATE", startDate);
            parameterSource.addValue("END_DATE", endDate);
        }
        return template.query(sqlFindAllSongsByFilter, parameterSource, songRowMapper);
    }

    @Override
    public Optional<Song> findById(Integer songId) {
        LOGGER.debug("SongDaoJdbc: findById({})",songId);
        SqlParameterSource parameterSource = new MapSqlParameterSource("SONG_ID",songId);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(template.query(sqlFindById, parameterSource, songRowMapper)));
    }

    @Override
    public Integer create(Song song) {
        LOGGER.debug("SongDaoJdbc: create({})",song);
        checkingThatSongIsUnique(song);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("SINGER", song.getSinger())
                .addValue("TITTLE", song.getTittle())
                .addValue("ALBUM", song.getAlbum())
                .addValue("REALISE_DATE", song.getRealiseDate());
        template.update(sqlCreate, parameterSource, keyHolder);

        Integer songId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        song.setSongId(songId);
        return songId;
    }

    @Override
    public Integer update(Song song) {
        LOGGER.debug("SongDaoJdbc: update({})",song);
        checkingThatSongIsUnique(song);

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("SONG_ID", song.getSongId())
                .addValue("SINGER", song.getSinger())
                .addValue("TITTLE", song.getTittle())
                .addValue("ALBUM", song.getAlbum())
                .addValue("REALISE_DATE", song.getRealiseDate());

        return template.update(sqlUpdate,parameterSource);
    }

    @Override
    public Integer delete(Integer songId) {
        LOGGER.debug("SongDaoJdbc: delete({})", songId);
        return template.update(sqlDelete,new MapSqlParameterSource("SONG_ID", songId));
    }

    private void checkingThatSongIsUnique(Song song){
                boolean checkWhatSongUnique = template.queryForObject(sqlCheckSongUnique,
                new MapSqlParameterSource("SINGER", song.getSinger()).addValue("TITTLE", song.getTittle()),
                Integer.class) == 0;
        if (!checkWhatSongUnique){
            LOGGER.warn("The same song is already exist {}", song);
            throw new IllegalArgumentException("Song is already exist");
        }
    }
}