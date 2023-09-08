package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.List;


@Slf4j
@Component
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public List<User> getUsers() {
        log.info("Пришел GET запрос /users с телом: {}");
        List<User> users = service.getUsers();
        log.info("Отправлен ответ для GET запроса /users с телом: {}", users);
        return users;
    }

    @PostMapping()
    public User add(@RequestBody User user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        check(user);
        User response = service.add(user);
        log.info("Отправлен ответ для POST запроса /users с телом: {}", response);
        return response;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        log.info("Пришел PUT запрос /users с телом: {}", user);
        check(user);
        User response = service.update(user);
        log.info("Отправлен ответ для PUT запроса /users с телом: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Пришел GET запрос /users с телом: {id}", id);
        User user = service.getUser(id);
        log.info("Отправлен ответ для GET запроса /users с телом: {id}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пришел PUT запрос /users/{id}/friends/{friendId}", id, friendId);
        service.addFriend(id, friendId);
        log.info("Отправлен ответ для PUT запроса /users/{id}/friends/{friendId}");

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, long friendId) {
        log.info("Пришел DELETE запрос /users/{id}/friends/{friendId}", id, friendId);
        service.deleteFriend(id, friendId);
        log.info("Отправлен ответ для DELETE запроса /users/{id}/friends/{friendId}");

    }

    @GetMapping("/{id}/friends")
    public List<User> friends(@PathVariable long id) {
        log.info("Пришел GET запрос /users/{id}/friends", id);
        List<User> friends = service.friends(id);
        log.info("Отправлен ответ для GET запроса /users/{id}/friends", friends);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Пришел GET запрос /users/{id}/friends/common/{otherId}", id, otherId);
        List<User> friends = service.mutualFriends(id, otherId);
        log.info("Отправлен ответ для GET запроса /users/{id}/friends/common/{otherId}", friends);
        return friends;
    }

    private void check(User user) {
        log.info("лог.пришел запрос Post /Users с телом: request");
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("имя для отображения может быть пустым — в таком случае будет использован логин");
            throw new ValidationException("имя для отображения может быть пустым — в таком случае будет использован логин");
        } else {
            log.info("отправлен ответ с телом: response");
        }
    }


}
