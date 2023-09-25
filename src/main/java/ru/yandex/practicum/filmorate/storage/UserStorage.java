package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


//
public interface UserStorage {
    List<User> getUsers();
    User add(User user);
    User update(User user);
    User getUser(int id);
    List<User> mutualFriends(long userId, long friendId);
    List<User> friends(long userId);
    void deleteFriend(long userId, long friendId);
    void addFriend(int userId, int friendId);
}
