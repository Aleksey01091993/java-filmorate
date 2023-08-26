package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film add(Film film) {
        storage.add(film);
        return film;
    }

    public Film update(Film film) {
        storage.update(film);
        return film;
    }

    public void addLike(long filmId, long userId) {
        storage.getFilms().get((int) filmId).getLikes().add(userId);
    }

    public Film getFilm(long id) {
        return storage.getFilm(id);
    }

    public void deleteLike(long filmId, long userId) {
        storage.getFilms().get((int) filmId).getLikes().remove(userId);
    }

    public List<Film> topFilms(Integer id) {
        int ids = 10;
        if (id != null) {
            ids = id;
        }
        List<Film> films = new ArrayList<>();
        List<Film> sort = storage.getFilms().stream()
                .sorted((o1, o2) -> o1.getLikes().size() - o2.getLikes().size())
                .collect(Collectors.toList());
        for (int i = 0; i < ids; i++) {
            films.add(sort.get(i));
        }
        return films;
    }
    public FilmStorage getStorage() {
        return storage;
    }


}
