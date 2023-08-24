package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class FilmService {
    private final User user;
    private final FilmStorage storage;

    @Autowired
    public FilmService(User user, FilmStorage storage) {
        this.user = user;
        this.storage = storage;
    }

    public FilmStorage getStorage() {
        return storage;
    }

    public User getUser() {
        return user;
    }
}
