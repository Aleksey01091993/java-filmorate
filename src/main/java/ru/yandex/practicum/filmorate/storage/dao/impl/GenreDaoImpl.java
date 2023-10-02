package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;

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
}
