package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.List;


@Slf4j
@Component
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService service;
    private final FriendsService friendsService;

    @Autowired
    public UserController(UserService service, FriendsService friendsService) {
        this.service = service;
        this.friendsService = friendsService;
    }

    @GetMapping()
    public List<User> getUsers() {
        log.info("Пришел GET запрос /users");
        List<User> users = service.getUsers();
        log.info("Отправлен ответ для GET запроса /users с телом: {}", users);
        return users;
    }

    @PostMapping()
    public User add(@RequestBody User user) {
        log.info("Пришел POST запрос /users с телом: {}", user);
        User response = service.add(user);
        log.info("Отправлен ответ для POST запроса /users с телом: {}", response);
        return response;
    }

    @PutMapping()
    public User update(@RequestBody User user) throws DataNotFoundException {
        log.info("Пришел PUT запрос /users с телом: {}", user);
        User response = service.update(user);
        log.info("Отправлен ответ для PUT запроса /users с телом: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) throws DataNotFoundException {
        log.info("Пришел GET запрос /users с телом: {}", id);
        User user = service.getUser(id);
        log.info("Отправлен ответ для GET запроса /users с телом: {}", user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пришел PUT запрос /users/{}/friends/{}", id, friendId);
        friendsService.addFriend(id, friendId);
        log.info("Отправлен ответ для PUT запроса /users/{}/friends/{}", id, friendId);

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, int friendId) {
        log.info("Пришел DELETE запрос /users/{}/friends/{}", id, friendId);
        friendsService.deleteFriend(id, friendId);
        log.info("Отправлен ответ для DELETE запроса /users/{}/friends/{}", id, friendId);

    }

    @GetMapping("/{id}/friends")
    public List<User> friends(@PathVariable int id) {
        log.info("Пришел GET запрос /users/{}/friends", id);
        List<User> friends = friendsService.friends(id);
        log.info("Отправлен ответ для GET запроса /users/{}/friends", id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable int id, @PathVariable int otherId) throws DataNotFoundException {
        log.info("Пришел GET запрос /users/{}/friends/common/{}", id, otherId);
        List<User> friends = friendsService.mutualFriends(id, otherId);
        log.info("Отправлен ответ для GET запроса /users/{}/friends/common/{}", id, otherId);
        return friends;
    }




}
