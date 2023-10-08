package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        return this.jdbcTemplate.query(
                "select * from FILMS f, MPA m where f.MPA_ID = m.MPA_ID", this::mapRow
        );
    }

    @Override
    public Film add(Film film) {
        jdbcTemplate.update(
                "insert into films values (?, ?, ?, ?, ?)",
                film.getName(),
                film.getMpaId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
        return film;
    }

    @Override
    public Film update(Film film) {
        if (getFilm(film.getId()) == null) {
            throw new ValidationException("Not found key: " + film.getId());
        }
        jdbcTemplate.update(
                "update films set name = ?, mpa_id = ?, description = ?, release_date = ?, duration = ? WHERE id = ?",
                film.getName(),
                film.getMpaId(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );
        return film;
    }

    @Override
    public Film getFilm(int id) {
        return jdbcTemplate.queryForObject(
                "select * from FILMS f, MPA m where f.MPA_ID = m.MPA_ID AND f.id = ?", this::mapRow, id
        );

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
                    "ORDER BY COUNT(like_users_id) DESC " +
                    "LIMIT ?;", (o1, o2) -> o1.getInt("film_id"), ids
        );
        List<Film> topFilms = new ArrayList<>();
        for (int i:film_id) {
            topFilms.add(getFilm(i));
        }
        return topFilms;
    }


    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setMpa(new MPA(rs.getInt("mpa_id"), rs.getString("age_limit")));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getFloat("duration"));

        return film;
    }
}
