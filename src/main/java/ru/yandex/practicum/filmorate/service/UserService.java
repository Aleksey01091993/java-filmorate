package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User getUser(long id) {
        return storage.getUser(id);
    }

    public User add(User user) {
        return storage.add(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        storage.getUsers().get(userId).getFriends().add(friendId);
        storage.getUsers().get(friendId).getFriends().add(userId);
    }

    public void deleteFriend(long userId, long friendId) {
        storage.getUsers().get((int) userId).getFriends().remove(friendId);
        storage.getUsers().get((int) friendId).getFriends().remove(userId);
    }

    public List<User> friends(long userId) {
        List<User> users = new ArrayList<>();
        for (User u : storage.getUsers()) {
            long ids = u.getId();
            if (storage.getUsers().get((int) userId).getFriends().contains(ids)) {
                users.add(u);
            }
        }
        return users;
    }

    public List<User> mutualFriends(long userId, long friendId) {
        List<User> users = new ArrayList<>();
        for (User u : storage.getUsers()) {
            long ids = u.getId();
            if (storage.getUsers().get((int) userId).getFriends().contains(ids) && storage.getUsers().get((int) friendId).getFriends().contains(ids)) {
                users.add(u);
            }
        }
        return users;
    }

}
