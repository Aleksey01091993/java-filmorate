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


}
