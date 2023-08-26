package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class FilmControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getFilms() {
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void addLike() {
    }

    @Test
    void deleteLike() {
    }

    @Test
    void getTopFilms() {
    }

    @Test
    void notFound() {
    }

    @Test
    void validationException() {
    }

    @Test
    void serverError() {
    }
}