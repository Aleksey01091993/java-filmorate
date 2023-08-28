package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
// убрал лишние импорты
//

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
        check(film);
        return service.add(film);
    }

    @PutMapping()
    public Film update(@Validated @RequestBody Film film) {
        check(film);
        return service.update(film);
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

    private void check(Film film) {
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
