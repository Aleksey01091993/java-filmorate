package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping(value = "/genres")
public class GenreController {

    private final GenreService service;

    @Autowired
    public GenreController(GenreService genreService) {
        this.service = genreService;
    }

    @GetMapping()
    public List<Genre> genres () {
        log.info("Пришел GET запрос /genres");
        List<Genre> genres = service.genres();
        log.info("Отправлен ответ для GET запроса /genres с телом: {}", genres);
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        log.info("Пришел GET запрос /genres с телом: {}", id);
        Genre genre = service.genre(id);
        log.info("Отправлен ответ для GET запроса /genres с телом: {}", genre);
        return genre;

    }
}
