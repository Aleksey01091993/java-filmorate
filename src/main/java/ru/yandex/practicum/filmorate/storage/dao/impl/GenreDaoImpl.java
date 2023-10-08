package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("GenreDaoImpl")
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> genres() {
        return  jdbcTemplate.query("SELECT * FROM GENRE",
                (o1, o2) -> new Genre(o1.getInt("id"), o1.getString("genre")));
    }

    @Override
    public Genre genre(int id) {
        return  jdbcTemplate.queryForObject("SELECT * FROM GENRE WHERE id = ?",
                (o1, o2) -> new Genre(o1.getInt("id"), o1.getString("genre")), id);
    }

    @Override
    public List<Genre> loadGenre(int id) {
        return jdbcTemplate.query("SELECT * FROM GENRE g, GENRE_FILM fg WHERE g.id = fg.genre AND film_id = ?",
                (o1, o2) -> new Genre(o1.getInt("id"), o1.getString("name")), id);
    }

    @Override
    public void load(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, p -> p));
        final String sqlQuery = "select * from GENRE g, GENRE_FILM fg where fg.GENRE = g.ID AND fg.FILM_ID in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getInt("FILM_ID"));
            film.getGenre().add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
        }, films.stream().map(Film::getId).toArray());
    }
}
