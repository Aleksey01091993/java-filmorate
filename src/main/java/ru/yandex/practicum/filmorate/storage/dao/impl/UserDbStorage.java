package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "select * from persons";
        return this.jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public User add(User user) {
        jdbcTemplate.update(
                "insert into persons values (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        return user;
    }

    @Override
    public User update(User user) throws DataNotFoundException {

        if (getUser(user.getId()) == null) {
            throw new ValidationException("Not found key: " + user.getId());
        }
        jdbcTemplate.update(
                "update persons set email = ?, login = ?, name = ?, birthday = ? WHERE id = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public User getUser(int id) throws DataNotFoundException {
        String sql = "select * from persons where id = ?";
        List<User> user = jdbcTemplate.query(sql, this::mapRow, id);
        if (user.size() != 1) {
            throw new DataNotFoundException("user id=" + id);
        }
        return user.get(0);
    }


    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        List<Integer> friends = jdbcTemplate.query(
                "select person_id from friends where person_id = " + rs.getInt("id"), (rl, yt) -> rl.getInt("user_id"));
        user.setFriends(new HashSet<>(friends));

        return user;
    }




}
