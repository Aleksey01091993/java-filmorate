package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final User user;
    private final UserStorage storage;

    @Autowired
    public UserService(User user, UserStorage storage) {
        this.user = user;
        this.storage = storage;
    }

    public User addFriend(long id) {
        user.getFriends().add(id);
        storage.update(user);
        storage.getUsers().get((int) id).getFriends().add(id);
        return user;
    }

    public User deleteFriend(long id) {
        user.getFriends().remove(id);
        storage.update(user);
        storage.getUsers().get((int) id).getFriends().remove(id);
        return user;
    }

    public List<User> mutualFriends(long id) {
        List<User> users = new ArrayList<>();
        for (User u: storage.getUsers()) {
            long ids = u.getId();
            if (user.getFriends().contains(ids) &&
                storage.getUsers().get((int) id).getFriends().contains(ids)) {
                users.add(u);
            }
        }
        return users;
    }

    public UserStorage getStorage() {
        return storage;
    }

    public User getUser() {
        return user;
    }
}
