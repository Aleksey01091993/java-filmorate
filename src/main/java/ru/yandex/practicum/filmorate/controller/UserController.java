package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ErrorHandler;
import lombok.extern.slf4j.Slf4j;
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
        return service.getUsers();
    }

    @PostMapping()
    public User add(@RequestBody User user) {
        service.add(user);
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        service.update(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return service.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, long friendId) {
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> friends(@PathVariable long id) {
        return service.friends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return service.mutualFriends(id, otherId);
    }


}
