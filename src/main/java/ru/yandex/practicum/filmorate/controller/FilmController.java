package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private static long counter = 0L;
    private final Map<Long, Film> films = new HashMap<>();


    @GetMapping()
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film add(@RequestBody Film film) {
        check(film);
        final long id = ++counter;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        if (films.get(film.getId()) == null) {
            throw new ValidationException("Not found key: " + film.getId());
        }
        check(film);
        films.put(film.getId(), film);
        return film;
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
