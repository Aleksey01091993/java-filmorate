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
        check(user);
        final long id = ++counter;
        user.setId(id);
        service.add(user);
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        check(user);
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(final ClassNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String validationException(final ValidationException e) {
        return e.getMessage();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String serverError(final Throwable e) {
        return e.getMessage();
    }
}
