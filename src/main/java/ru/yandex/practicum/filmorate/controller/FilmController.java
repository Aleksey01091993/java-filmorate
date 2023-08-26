package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private static long counter = 0L;
    private final FilmService service;

    @Autowired
    public FilmController(FilmService filmService) {
        this.service = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return service.getFilm(id);
    }

    @GetMapping()
    public List<Film> getFilms() {
        return service.getFilms();
    }

    @PostMapping()
    public Film add(@RequestBody Film film) {
        check(film);
        final long id = ++counter;
        film.setId(id);
        service.add(film);
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        check(film);
        service.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getTopFilms(@PathVariable(required = false) int count) {
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(final ClassNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String validationException (final ValidationException e) {
        return e.getMessage();
    }



    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serverError(final Throwable e) {
        return e.getMessage();
    }
}
