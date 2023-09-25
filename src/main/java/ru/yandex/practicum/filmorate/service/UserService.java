package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    private final UserStorage storage;

    public UserService(@Autowired @Qualifier("UserDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User getUser(int id) {
        return storage.getUser(id);
    }

    public User add(User user) {
        return storage.add(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        storage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        deleteFriend(userId, friendId);
    }

    public List<User> friends(long userId) {
        return storage.friends(userId);
    }

    public List<User> mutualFriends(long userId, long friendId) {
        return storage.mutualFriends(userId, friendId);
    }

}
