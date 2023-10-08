package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;
import ru.yandex.practicum.filmorate.storage.dao.impl.LikesDaoImpl;

@Service
public class LikesService {

    private final LikesDao likesDao;
    private final FilmStorage storage;
    private final UserStorage userStorage;

    public LikesService(@Autowired @Qualifier(value = "LikesDaoImpl") LikesDaoImpl likesDao,
                        @Autowired @Qualifier(value = "FilmDbStorage") FilmStorage storage,
                        @Autowired @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.likesDao = likesDao;
        this.storage = storage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        final Film film = storage.getFilm(filmId);
        final User user = userStorage.getUser(userId);
        likesDao.addLike(film.getId(), user.getId());
    }

    public void deleteLike(long filmId, long userId) {
        likesDao.deleteLike(filmId, userId);
    }

}
