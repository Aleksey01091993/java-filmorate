package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.time.LocalDate;
import java.util.List;


@Slf4j
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
        check(film);
        return storage.add(film);
    }

    public Film update(Film film) {
        check(film);
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

    private void check(Film film) {
        log.info("лог.пришел запрос Post /films с телом: request");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))) {
            log.debug("дата релиза — не раньше 28.12.1895");
            throw new ValidationException("дата релиза — не раньше 28.12.1895");
        } else {
            log.info("отправлен ответ с телом: response");
        }
    }

}
