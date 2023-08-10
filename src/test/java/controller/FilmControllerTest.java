package controller;

import exeption.ValidationException;
import model.Film;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerTest {

    @Test
    void getFilms() {
        FilmController filmController = new FilmController();
        Film film = new Film(0, "lex", "hjkhk", LocalDate.now(), Duration.of(2, ChronoUnit.HOURS));
        filmController.add(film);
        List<Film> films = filmController.getFilms();
        Film film1 = films.get(0);
        assertEquals(film1, film);
    }

    @Test
    void add() {
        FilmController filmController = new FilmController();
        Film film = new Film(0, "", "hjkhk", LocalDate.now(), Duration.of(2, ChronoUnit.HOURS));
        try {
            filmController.add(film);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "название не может быть пустым");
        }
        Film film1 = new Film(0, "lex", "hjkhkkkkkkkkkkkkkkkkkkkkkkkkkkk" +
                "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" +
                "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" +
                "lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "llllllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "llllllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "llllllllllllllllllllllllllllllllllllllllllllllll" +
                "llllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "llllllllllllllllllllllllllllllllllllllllllllllllllllllll" +
                "llllllllllllll", LocalDate.now(), Duration.of(2, ChronoUnit.HOURS));
        try {
            filmController.add(film1);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "максимальная длина описания — 200 символов");
        }
        Film film2 = new Film(0, "hj", "hjkhk", LocalDate.of(1895, 12, 27), Duration.of(2, ChronoUnit.HOURS));
        try {
            filmController.add(film2);
        } catch (ValidationException e){
            String message = e.getMessage();
            System.out.println(message);
            assertEquals(message, "дата релиза — не раньше 28.12.1895");
        }

        Film film3 = new Film(0, "lex", "hjkhk", LocalDate.now(), Duration.of(0, ChronoUnit.HOURS));
        try {
            filmController.add(film3);
        } catch (ValidationException e){
            String message = e.getMessage();
            assertEquals(message, "продолжительность фильма должна быть положительной");
        }
    }
}