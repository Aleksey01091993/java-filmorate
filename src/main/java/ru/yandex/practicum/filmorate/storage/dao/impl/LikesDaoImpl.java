package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;

@Component("LikesDaoImpl")
public class LikesDaoImpl implements LikesDao {

    private final JdbcTemplate jdbcTemplate;

    public LikesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteLike(long filmId, long userId) {
            jdbcTemplate.update(
                    "DELETE FROM likes WHERE film_id = ? and like_user_id = ?", filmId, userId
            );
    }

    @Override
    public void addLike(int filmId, int userId) {
            jdbcTemplate.update(
                    "insert into likes values (?, ?)", filmId, userId
            );
    }
}
