package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                "insert into films(" +
                        "id, " +
                        "mpa, " +
                        "genre, " +
                        "name, " +
                        "description, " +
                        "release_date, " +
                        "duration, " +
                        "likes) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getMpa(),
                film.getGenre(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes()
        );
        return film;
    }

    @Override
    public Film update(Film film) {
        if (getFilm(film.getId()) == null) {
            throw new ValidationException("Not found key: " + film.getId());
        }
        jdbcTemplate.update(
                "insert into films(" +
                        "id, " +
                        "mpa, " +
                        "genre, " +
                        "name, " +
                        "description, " +
                        "release_date, " +
                        "duration, " +
                        "likes) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getMpa(),
                film.getGenre(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes()
        );
        return film;
    }

    @Override
    public Film getFilm(int id) {
        return jdbcTemplate.queryForObject(
                "select * from films where id = ?", this::mapRow, id
        );

    }


    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setMpa(rs.getString("mpa"));
        film.setGenre(rs.getString("genre"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getFloat("duration"));
        List<Integer> likes = jdbcTemplate.query(
                "select user_id from likes where film_id = " + rs.getInt("id"), (rl, yt) -> rl.getInt("user_id"));
        film.setLikes(new HashSet<>(likes));

        return film;
    }
}
