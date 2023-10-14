package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {
    void addFriend(int userId, int friendId);
    void deleteFriend(long userId, long friendId);
    List<User> friends(long userId);
    List<User> mutualFriends(long userId, long friendId) throws DataNotFoundException;

}
