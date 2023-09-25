package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private int counter = 0;

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
        final int id = ++counter;
        user.setId(id);
        jdbcTemplate.update(
                "insert into persons values (?, ?, ?, ?, ?)",
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        return user;
    }

    @Override
    public User update(User user) {
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
    public User getUser(int id) {
        String sql = "select * from persons where id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRow, id);
    }

    @Override
    public List<User> mutualFriends(long userId, long friendId) {
        List<Integer> idFriends = jdbcTemplate.query("SELECT friends_id FROM friends where person_id = " + userId,
                (o1, o2) -> o1.getInt("friends_id"));
        List<Integer> idFriends2 = jdbcTemplate.query("SELECT friends_id FROM friends where person_id = " + friendId,
                (o1, o2) -> o1.getInt("friends_id"));
        List<User> mutualFriends = new ArrayList<>();
        for (int i: idFriends) {
            for (int u: idFriends2) {
                if (i == u) {
                    mutualFriends.add(getUser(i));
                }
            }
        }
        return mutualFriends;
    }

    @Override
    public List<User> friends(long userId) {
        String sql = "SELECT * FROM persons WHERE id IN (SELECT friends_id FROM friends WHERE person_id = ?)";
        return jdbcTemplate.query(sql, this::mapRow, userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {

        jdbcTemplate.update("DELETE FROM friends WHERE person_id = ? AND friends_id = ?", userId, friendId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        Integer isCount;
            isCount = jdbcTemplate.queryForObject(
                            "SELECT COUNT(MUTUAL_FRIENDS) FROM friend_request WHERE person_id = ?",
                            Integer.class, userId);
        boolean is = false;
            if (isCount == 0) {
                jdbcTemplate.update("INSERT INTO FRIENDS VALUES (?, ?)", userId, friendId);
                jdbcTemplate.update("insert into friend_request values (?, ?, ?)", friendId, is, userId);
            } else {
                is = true;
                jdbcTemplate.update("INSERT INTO FRIENDS VALUES (?, ?)", friendId, userId);
                jdbcTemplate.update("update friend_request set mutual_friends = ? WHERE person_id = ?", is, userId);
            }

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
