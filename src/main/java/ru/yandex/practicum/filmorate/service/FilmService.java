package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class FilmService {
    private final FilmStorage storage;

    public FilmService(@Autowired @Qualifier(value = "FilmDbStorage") FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.add(film);
    }

    public void addLike(int filmId, int userId) {
        storage.addLike(filmId, userId);
    }

    public Film getFilm(int id) {
        return storage.getFilm(id);
    }

    public void deleteLike(long filmId, long userId) {
        storage.deleteLike(filmId, userId);
    }

    public List<Film> topFilms(Integer id) {
        return storage.topFilms(id);
    }


}
