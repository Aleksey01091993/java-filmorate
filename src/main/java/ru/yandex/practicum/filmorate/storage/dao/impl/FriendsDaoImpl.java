package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component("FriendsDaoImpl")
public class FriendsDaoImpl implements FriendsDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate,
                          @Autowired @Qualifier(value = "UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> mutualFriends(long userId, long friendId) throws DataNotFoundException {
        List<Integer> idFriends = jdbcTemplate.query("SELECT friend_id FROM friend_request where user_id = " + userId,
                (o1, o2) -> o1.getInt("friend_id"));
        List<Integer> idFriends2 = jdbcTemplate.query("SELECT friend_id FROM friend_request where user_id = " + friendId,
                (o1, o2) -> o1.getInt("friend_id"));
        List<User> mutualFriends = new ArrayList<>();
        for (int i: idFriends) {
            for (int u: idFriends2) {
                if (i == u) {
                    mutualFriends.add(userStorage.getUser(i));
                }
            }
        }
        return mutualFriends;
    }

    @Override
    public List<User> friends(long userId) {
        Integer isCount;
        isCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(MUTUAL_FRIENDS) FROM friend_request WHERE user_id = ?",
                Integer.class, userId);
        if (isCount == 0) {
            throw new ValidationException("ваш список друзей пуст");
        } else {
            String sql = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friend_request WHERE user_id = ?)";
            return jdbcTemplate.query(sql, this::mapRow, userId);
        }

    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        Integer isUser = -1;
        isUser = jdbcTemplate.queryForObject(
                "SELECT COUNT(MUTUAL_FRIENDS) FROM friend_request WHERE friend_id = ? AND user_id = ?",
                Integer.class, friendId, userId);
        Integer isFriend = -1;
        isFriend = jdbcTemplate.queryForObject(
                "SELECT COUNT(MUTUAL_FRIENDS) FROM friend_request WHERE friend_id = ? AND user_id = ?",
                Integer.class, userId, friendId);
        if (isFriend == 1 && isUser == 1) {
            jdbcTemplate.update("DELETE FROM friend_request WHERE user_id = ?", userId);
            jdbcTemplate.update("update friend_request set mutual_friends = ? WHERE user_id = ?",
                    Boolean.FALSE, friendId);
        } else if (isFriend == 0 && isUser == 1) {
            jdbcTemplate.update("DELETE FROM friend_request WHERE user_id = ?", userId);
        } else {
            throw new ValidationException("пользователь не является вашим другом или" +
                    " неверно введены данные повторите папытку снова");
        }


    }

    @Override
    public void addFriend(int userId, int friendId) {
        boolean is = false;
        Integer isUser = -1;
        isUser = jdbcTemplate.queryForObject(
                "SELECT COUNT(MUTUAL_FRIENDS) FROM friend_request WHERE friend_id = ? AND user_id = ?",
                Integer.class, friendId, userId);
        Integer isFriend = -1;
        isFriend = jdbcTemplate.queryForObject(
                "SELECT COUNT(MUTUAL_FRIENDS) FROM friend_request WHERE friend_id = ? AND user_id = ?",
                Integer.class, userId, friendId);
        if (isFriend == 0 && isUser == 0) {
            jdbcTemplate.update("insert into friend_request values (?, ?, ?)", userId, is, friendId);
        } else if (isUser == 0 && isFriend == 1) {
            is = true;
            jdbcTemplate.update("insert into friend_request values (?, ?, ?)", userId, is, friendId);
            jdbcTemplate.update("update friend_request set mutual_friends = ? WHERE user_id = ? AND friend_id = ?",
                    is, friendId, userId);
        } else {
            throw new ValidationException("вы уже являетесь друзьями или неверно веденны данные попробуйте снова");
        }


    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }


}
