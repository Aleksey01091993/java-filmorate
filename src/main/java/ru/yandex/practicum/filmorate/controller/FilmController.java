package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.time.LocalDate;
import java.util.List;
//

@Slf4j
@Component
@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private final FilmService service;

    @Autowired
    public FilmController(FilmService filmService) {
        this.service = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Пришел GET запрос /films с телом: {id}", id);
        Film film = service.getFilm(id);
        log.info("Отправлен ответ для GET запроса /films с телом: {id}", film);
        return film;

    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("Пришел GET запрос /films с телом: {}");
        List<Film> films = service.getFilms();
        log.info("Отправлен ответ для GET запроса /films с телом: {}", films);
        return films;
    }

    @PostMapping()
    public Film add(@Validated @RequestBody Film film) {
        log.info("Пришел POST запрос /films с телом: {}", film);
        check(film);
        Film response = service.add(film);
        log.info("Отправлен ответ для POST запроса /films с телом: {}", response);
        return response;
    }

    @PutMapping()
    public Film update(@Validated @RequestBody Film film) {
        log.info("Пришел PUT запрос /films с телом: {}", film);
        check(film);
        Film response = service.update(film);
        log.info("Отправлен ответ для PUT запроса /films с телом: {}", response);
        return response;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пришел PUT запрос /films/{id}/like/{userId}", id, userId);
        service.addLike(id, userId);
        log.info("Отправлен ответ для PUT запроса /films/{id}/like/{userId}");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пришел DELETE запрос /films/{id}/like/{userId}", id, userId);
        service.deleteLike(id, userId);
        log.info("Отправлен ответ для DELETE запроса /films/{id}/like/{userId}");
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getTopFilms(@PathVariable(required = false) int count) {
        log.info("Пришел GET запрос /films/popular?count={count}", count);
        List<Film> films = service.topFilms(count);
        log.info("Отправлен ответ для GET запроса /films/popular?count={count}", films);
        return films;
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
