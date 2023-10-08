package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikesService;


import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private final FilmService service;
    private final LikesService likesService;

    @Autowired
    public FilmController(FilmService filmService, LikesService likesService) {
        this.service = filmService;
        this.likesService = likesService;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Пришел GET запрос /films с телом: {}", id);
        Film film = service.getFilm(id);
        log.info("Отправлен ответ для GET запроса /films с телом: {}", film);
        return film;

    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("Пришел GET запрос /films");
        List<Film> films = service.getFilms();
        log.info("Отправлен ответ для GET запроса /films с телом: {}", films);
        return films;
    }

    @PostMapping()
    public Film add(@Validated @RequestBody Film film) {
        log.info("Пришел POST запрос /films с телом: {}", film);
        Film response = service.add(film);
        log.info("Отправлен ответ для POST запроса /films с телом: {}", response);
        return response;
    }

    @PutMapping()
    public Film update(@Validated @RequestBody Film film) {
        log.info("Пришел PUT запрос /films с телом: {}", film);
        Film response = service.update(film);
        log.info("Отправлен ответ для PUT запроса /films с телом: {}", response);
        return response;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пришел PUT запрос /films/{}/like/{}", id, userId);
        likesService.addLike(id, userId);
        log.info("Отправлен ответ для PUT запроса /films/{}/like/{}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пришел DELETE запрос /films/{}/like/{}", id, userId);
        likesService.deleteLike(id, userId);
        log.info("Отправлен ответ для DELETE запроса /films/{}/like/{}", id, userId);
    }

    @GetMapping("/popular?count={count}")
    public List<Film> getTopFilms(@PathVariable(required = false) int count) {
        log.info("Пришел GET запрос /films/popular?count={}", count);
        List<Film> films = service.topFilms(count);
        log.info("Отправлен ответ для GET запроса /films/popular?count={}", films);
        return films;
    }








}
