package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int counter = 0;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        final int id = ++counter;
        user.setId(id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.get(user.getId()) == null) {
            throw new ValidationException("Not found key: " + user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        getUsers().get(userId).getFriends().add(friendId);
        getUsers().get(friendId).getFriends().add(userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        getUsers().get((int) userId).getFriends().remove(friendId);
        getUsers().get((int) friendId).getFriends().remove(userId);
    }

    @Override
    public List<User> friends(long userId) {
        List<User> users = new ArrayList<>();
        for (User u : getUsers()) {
            long ids = u.getId();
            if (getUsers().get((int) userId).getFriends().contains(ids)) {
                users.add(u);
            }
        }
        return users;
    }

    @Override
    public List<User> mutualFriends(long userId, long friendId) {
        List<User> users = new ArrayList<>();
        for (User u : getUsers()) {
            long ids = u.getId();
            if (getUsers().get((int) userId).getFriends().contains(ids) &&
                    getUsers().get((int) friendId).getFriends().contains(ids)) {
                users.add(u);
            }
        }
        return users;
    }


}
