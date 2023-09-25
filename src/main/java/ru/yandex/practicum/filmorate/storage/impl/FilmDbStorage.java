package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
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
//
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
                        " values (?, ?, ?, ?, ?, ?, ?, ?)",
                film.getId(),
                film.getMpa(),
                film.getGenre(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes().size()
        );
        return film;
    }

    @Override
    public Film update(Film film) {
        if (getFilm(film.getId()) == null) {
            throw new ValidationException("Not found key: " + film.getId());
        }
        jdbcTemplate.update(
                "update films set " +
                        "mpa = " + film.getMpa() +
                        ", genre = " + film.getGenre() +
                        ", name = " + film.getName() +
                        ", description = " + film.getDescription() +
                        ", release_date = " + film.getReleaseDate() +
                        ", duration = " + film.getDuration() +
                        ", likes = ) " + film.getLikes().size() +
                        " where id = " + film.getId()
        );
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
        jdbcTemplate.update(
                "insert into likes values (?, ?)", filmId, userId
        );
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update(
                "DELETE FROM likes WHERE film_id = ? and friends_id = ?", filmId, userId
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
                    "ORDER BY COUNT(like_person_id) DESC " +
                    "LIMIT ?;", (o1, o2) -> o1.getInt("film_id"), ids
        );
        List<Film> topFilms = new ArrayList<>();
        for (int i:film_id) {
            topFilms.add(getFilm(i));
        }
        return topFilms;
    }


    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<String> genre = jdbcTemplate.query(
                "SELECT c.genre " +
                        "FROM GENRE AS c " +
                        "JOIN GENRE_FILM AS v ON c.id = v.genre " +
                        "WHERE v.film_id =" +
                        rs.getInt("id"),
                (o1, o2) -> o1.getString("c.genre")
        );
        List<Integer> likes = jdbcTemplate.query(
                "select user_id from likes where film_id = " +
                        rs.getInt("id"), (rl, yt) -> rl.getInt("user_id"));

        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setMpa(rs.getString("mpa"));
        film.setGenre(new ArrayList<>(genre));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getFloat("duration"));
        film.setLikes(new HashSet<>(likes));

        return film;
    }
}
