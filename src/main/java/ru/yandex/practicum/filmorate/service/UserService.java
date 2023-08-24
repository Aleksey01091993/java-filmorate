package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserService {

    private final User user;
    private final UserStorage storage;

    @Autowired
    public UserService(User user, UserStorage storage) {
        this.user = user;
        this.storage = storage;
    }

    public UserStorage getStorage() {
        return storage;
    }

    public User getUser() {
        return user;
    }
}
