package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;


import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int counter = 0;

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film add(Film film) {
        final int id = ++counter;
        film.setId(id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.get(film.getId()) == null) {
            throw new ValidationException("Not found key: " + film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public List<Film> topFilms(Integer id) {
        int ids = 10;
        if (id != null) {
            ids = id;
        }
        List<Film> films = new ArrayList<>(
        getFilms().stream().sorted(Comparator.comparingInt(o -> o.getLikes().size())).toList()
        );
        for (int i = 0; i < ids; i++) {
            films.add(films.get(i));
        }
        return films;
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        getFilms().get((int) filmId).getLikes().remove(userId);
    }

    @Override
    public void addLike(int filmId, int userId) {
        getFilms().get(filmId).getLikes().add(userId);
    }

}
