package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void getUsers() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("ghf@mail.ru", "lex", "lex", LocalDate.of(1993, 9, 01));
        userController.add(user);
        List<User> users = userController.getUsers();
        User user1 = users.get(0);
        assertEquals(user1, user);
    }

    @Test
    void user() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("ghfmail.ru", "lex", "lex", LocalDate.of(1993, 9, 01));
        try {
            userController.add(user);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "электронная почта не может быть пустой и должна содержать символ @");
        }
        User user1 = new User("", "lex", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user1);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "электронная почта не может быть пустой и должна содержать символ @");
        }
        User user2 = new User("ghf@mail.ru", "le x", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user2);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "логин не может быть пустым и содержать пробелы");
        }
        User user3 = new User("ghf@mail.ru", "", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user3);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "логин не может быть пустым и содержать пробелы");
        }
        User user4 = new User("ghf@mail.ru", "wed", "", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user4);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "имя для отображения может быть пустым — в таком случае будет использован логин");
            List<User> users = userController.getUsers();
            User user5 = users.get(0);
            User user7 = user4;
            user7.setName(user4.getLogin());
            assertEquals(user5, user7);
        }
        User user6 = new User("ghf@mail.ru", "dsdwe", "lex", LocalDate.of(3000, 9, 1));
        try {
            userController.add(user6);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "дата рождения не может быть в будущем");
        }


    }

    @Test
    void update() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("ghfmail.ru", "lex", "lex", LocalDate.of(1993, 9, 01));
        try {
            userController.add(user);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "электронная почта не может быть пустой и должна содержать символ @");
        }
        User user1 = new User("", "lex", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user1);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "электронная почта не может быть пустой и должна содержать символ @");
        }
        User user2 = new User("ghf@mail.ru", "le x", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user2);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "логин не может быть пустым и содержать пробелы");
        }
        User user3 = new User("ghf@mail.ru", "", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user3);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "логин не может быть пустым и содержать пробелы");
        }
        User user4 = new User("ghf@mail.ru", "wed", "", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user4);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "имя для отображения может быть пустым — в таком случае будет использован логин");
            List<User> users = userController.getUsers();
            User user5 = users.get(0);
            User user7 = user4;
            user7.setName(user4.getLogin());
            assertEquals(user5, user7);
        }
        User user6 = new User("ghf@mail.ru", "dsdwe", "lex", LocalDate.of(3000, 9, 1));
        try {
            userController.add(user6);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "дата рождения не может быть в будущем");
        }
        user6.setId(100);
        try {
            userController.update(user6);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "Not found key: " + user6.getId());
        }
    }
}