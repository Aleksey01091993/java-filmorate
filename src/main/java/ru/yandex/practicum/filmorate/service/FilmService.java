package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final User user;
    private final FilmStorage storage;

    @Autowired
    public FilmService(User user, FilmStorage storage) {
        this.user = user;
        this.storage = storage;
    }

    public Film addFilm(long id) {
        storage.getFilms().get((int) id).getLikes().add(user.getId());
        return storage.getFilms().get((int) id);
    }

    public Film deleteFilm(long id) {
        storage.getFilms().get((int) id).getLikes().remove(user.getId());
        return storage.getFilms().get((int) id);
    }

    public List<Film> topFilms(int id) {
        List<Film> films = new ArrayList<>();
        List<Film> sort = storage.getFilms().stream()
                .sorted((o1, o2) -> o1.getLikes().size() - o2.getLikes().size())
                .collect(Collectors.toList());
        for (int i = 0; i < id; i++) {
            films.add(sort.get(i));
        }
        return films;
    }
    public FilmStorage getStorage() {
        return storage;
    }

    public User getUser() {
        return user;
    }
}
