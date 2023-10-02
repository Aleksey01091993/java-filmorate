package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping(value = "/mpa")
public class MpaController {

    private final MpaService service;

    @Autowired
    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping()
    public List<MPA> allMpa () {
        log.info("Пришел GET запрос /mpa");
        List<MPA> mpa = service.allMpa();
        log.info("Отправлен ответ для GET запроса /mpa с телом: {}", mpa);
        return mpa;
    }

    @GetMapping("/{id}")
    public MPA getMpa(@PathVariable int id) {
        log.info("Пришел GET запрос /mpa с телом: {}", id);
        MPA mpa = service.mpa(id);
        log.info("Отправлен ответ для GET запроса /mpa с телом: {}", mpa);
        return mpa;
    }
}
