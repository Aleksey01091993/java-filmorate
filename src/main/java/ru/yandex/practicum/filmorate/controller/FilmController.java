package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private static long counter = 0L;
    private final FilmStorage storage;
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
        this.storage = service.getStorage();
    }

    @GetMapping()
    public List<Film> getFilms() {
        return storage.getFilms();
    }

    @PostMapping()
    public Film add(@RequestBody Film film) {
        check(film);
        final long id = ++counter;
        film.setId(id);
        storage.add(film);
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        check(film);
        storage.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Map<String, Long> path) {
        long filmId = path.get("id");
        long userId = path.get("userId");
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Map<String, Long> path) {
        long filmId = path.get("id");
        long userId = path.get("userId");
        service.deleteLike(filmId, userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getTopFilms(@PathVariable(required = false) Integer count) {
        return service.topFilms(count);
    }

    private void check (Film film) {
        log.info("лог.пришел запрос Post /films с телом: request");
        if (film.getName().isEmpty()) {
            log.debug("название не может быть пустым");
            throw new ValidationException("название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.debug("максимальная длина описания — 200 символов");
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 27))) {
            log.debug("дата релиза — не раньше 28.12.1895");
            throw new ValidationException("дата релиза — не раньше 28.12.1895");
        } else if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.debug("продолжительность фильма должна быть положительной");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        } else {
            log.info("отправлен ответ с телом: response");
        }
    }
}
