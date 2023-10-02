package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private int counter = 0;

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        return this.jdbcTemplate.query(
                "select * from films", this::mapRow
        );
    }

    @Override
    public Film add(Film film) {
        final int id = ++counter;
        film.setId(id);
        jdbcTemplate.update(
                "insert into films values (?, ?, ?, ?, ?)",
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
        jdbcTemplate.update(
                "insert into mpa_film values (?, ?)",
                film.getId(),
                film.getMpa()
        );
        for (Integer i:film.getGenre()) {
            jdbcTemplate.update(
                    "insert into genre_film values (?, ?)",
                    film.getId(),
                    i
            );
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (getFilm(film.getId()) == null) {
            throw new ValidationException("Not found key: " + film.getId());
        }
        jdbcTemplate.update(
                "update films set name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        jdbcTemplate.update(
                "update mpa_film set mpa = ? WHERE film_id = ?",
                film.getMpa(),
                film.getId()
        );
        jdbcTemplate.update("DELETE FROM genre_film WHERE film_id = ?", film.getId());
        for (Integer i:film.getGenre()) {
            jdbcTemplate.update(
                    "insert into genre_film values (?, ?)",
                    film.getId(),
                    i
            );
        }
        return film;
    }

    @Override
    public Film getFilm(int id) {
        return jdbcTemplate.queryForObject(
                "select * from films where id = ?", this::mapRow, id
        );

    }

    @Override
    public void addLike(int filmId, int userId) {
        Integer isUser = -1;
        isUser = jdbcTemplate.queryForObject(
                "SELECT COUNT(film_id) FROM likes WHERE like_person_id = ? AND film_id = ?",
                Integer.class, userId, filmId);
        if (isUser == 0) {
            jdbcTemplate.update(
                    "insert into likes values (?, ?)", filmId, userId
            );
        }

    }

    @Override
    public void deleteLike(long filmId, long userId) {
        Integer isUser = -1;
        isUser = jdbcTemplate.queryForObject(
                "SELECT COUNT(film_id) FROM likes WHERE like_person_id = ? AND film_id = ?",
                Integer.class, userId, filmId);
        if (isUser == 1) {
            jdbcTemplate.update(
                    "DELETE FROM likes WHERE film_id = ? and like_person_id = ?", filmId, userId
            );
        }

    }

    @Override
    public List<Film> topFilms(Integer id) {
        int ids = 10;
        if (id != null) {
            ids = id;
        }

        List<Integer> film_id = jdbcTemplate.query(
                "SELECT film_id " +
                    "FROM likes " +
                    "GROUP BY film_id " +
                    "ORDER BY COUNT(like_person_id) DESC " +
                    "LIMIT ?;", (o1, o2) -> o1.getInt("film_id"), ids
        );
        List<Film> topFilms = new ArrayList<>();
        for (int i:film_id) {
            topFilms.add(getFilm(i));
        }
        return topFilms;
    }

    public List<Genre> genres() {
        return  jdbcTemplate.query("SELECT * FROM GENRE",
                (o1, o2) -> new Genre(o1.getInt("id"), o1.getString("genre")));
    }

    public Genre genre(int id) {
        return  jdbcTemplate.queryForObject("SELECT * FROM GENRE WHERE id = ?",
                (o1, o2) -> new Genre(o1.getInt("id"), o1.getString("genre")), id);
    }

    public List<MPA> allMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa",
                (o1, o2) -> new MPA(o1.getInt("id"), o1.getString("mpa")));
    }

    public MPA mpa(int id) {
        return  jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = ?",
                (o1, o2) -> new MPA(o1.getInt("id"), o1.getString("mpa")), id);
    }


    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Integer> genre = jdbcTemplate.query("SELECT genre FROM genre_film WHERE film_id = ?",
                (o1, o2) -> o1.getInt("genre"), rs.getInt("id")
        );
        List<Integer> likes = jdbcTemplate.query(
                "select like_person_id from likes where film_id = ?", (rl, yt) -> rl.getInt("like_person_id"),
                rs.getInt("id"));
        SqlRowSet mpa = jdbcTemplate.queryForRowSet("SELECT mpa FROM mpa_film WHERE film_id = ?",
                rs.getInt("id"));
        Film film = new Film();
        film.setId(rs.getInt("id"));
        if (mpa.next()) {
            film.setMpa(mpa.getInt("mpa"));
        } else {
            film.setMpa(null);
        }
        film.setGenre(new ArrayList<>(genre));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getFloat("duration"));
        film.setLikes(new HashSet<>(likes));

        return film;
    }
}
