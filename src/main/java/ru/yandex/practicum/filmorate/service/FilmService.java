package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;
import ru.yandex.practicum.filmorate.storage.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.storage.dao.impl.LikesDaoImpl;


import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
public class FilmService {
    private final FilmStorage storage;
    private final GenreDao genreDao;


    public FilmService(@Autowired @Qualifier(value = "FilmDbStorage") FilmStorage storage,
                       @Autowired @Qualifier(value = "GenreDaoImpl") GenreDaoImpl genreDao
                       ) {
        this.storage = storage;
        this.genreDao = genreDao;

    }

    public List<Film> getFilms() {
        final List<Film> films = storage.getFilms();
        genreDao.load(films);
        return films;
    }

    public Film add(Film film) {
        check(film);
        return storage.add(film);
    }

    public Film update(Film film) {
        check(film);
        return storage.add(film);
    }



    public Film getFilm(int id) {
        Film film = storage.getFilm(id);
        film.setGenre(genreDao.loadGenre(id));
        return film;
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
