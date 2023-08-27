package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exeption.ErrorHandler;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;


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
    public Film getFilm(@PathVariable long id) {
        return service.getFilm(id);
    }

    @GetMapping()
    public List<Film> getFilms() {
        return service.getFilms();
    }

    @PostMapping()
    public Film add(@Validated @RequestBody Film film) {
        service.add(film);
        return film;
    }

    @PutMapping()
    public Film update(@Validated @RequestBody Film film) {
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


}
