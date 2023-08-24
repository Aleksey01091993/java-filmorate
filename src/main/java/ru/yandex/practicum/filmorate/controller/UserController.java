package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private static long counter = 0L;
    private final UserStorage storage;
    private final UserService service;

    @Autowired
    public UserController() {
        this.service = new UserService(new InMemoryUserStorage());
        this.storage = service.getStorage();
    }

    @GetMapping()
    public List<User> getUsers() {
        return storage.getUsers();
    }

    @PostMapping()
    public User add(@RequestBody User user) {
        check(user);
        final long id = ++counter;
        user.setId(id);
        storage.add(user);
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        check(user);
        storage.update(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Map<String, Long> path) {
        long userId = path.get("id");
        long friendId = path.get("friendId");
        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Map<String, Long> path) {
        long userId = path.get("id");
        long friendId = path.get("friendId");
        service.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> friends(@PathVariable long id) {
        return service.friends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable Map<String, Long> path) {
        long userId = path.get("id");
        long friendId = path.get("otherId");
        return service.mutualFriends(userId, friendId);
    }


    private void check(User user) {
        log.info("лог.пришел запрос Post /films с телом: request");
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.debug("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.debug("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("имя для отображения может быть пустым — в таком случае будет использован логин");
            throw new ValidationException("имя для отображения может быть пустым — в таком случае будет использован логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        } else {
            log.info("отправлен ответ с телом: response");
        }
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(final ClassNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationException(final ValidationException e) {
        return Map.of("validationException", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serverError(final Throwable e) {
        return e.getMessage();
    }
}
