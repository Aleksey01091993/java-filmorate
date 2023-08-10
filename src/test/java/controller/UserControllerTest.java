package controller;

import exeption.ValidationException;
import model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void getUsers() {
        UserController userController = new UserController();
        User user = new User(0, "ghf@mail.ru", "lex", "lex", LocalDate.of(1993, 9, 01));
        userController.add(user);
        List<User> users = userController.getUsers();
        User user1 = users.get(0);
        assertEquals(user1, user);
    }

    @Test
    void user() {
        UserController userController = new UserController();
        User user = new User(0, "ghfmail.ru", "lex", "lex", LocalDate.of(1993, 9, 01));
        try {
            userController.add(user);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "электронная почта не может быть пустой и должна содержать символ @");
        }
        User user1 = new User(0, "", "lex", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user1);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "электронная почта не может быть пустой и должна содержать символ @");
        }
        User user2 = new User(0, "ghf@mail.ru", "le x", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user2);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "логин не может быть пустым и содержать пробелы");
        }
        User user3 = new User(0, "ghf@mail.ru", "", "lex", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user3);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "логин не может быть пустым и содержать пробелы");
        }
        User user4 = new User(0, "ghf@mail.ru", "wed", "", LocalDate.of(1993, 9, 1));
        try {
            userController.add(user4);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "имя для отображения может быть пустым — в таком случае будет использован логин");
            List<User> users = userController.getUsers();
            User user5 = users.get(0);
            assertEquals(user5, new User(user4.getId(), user4.getEmail(), user4.getLogin(), user4.getLogin(), user4.getBirthday()));
        }
        User user6 = new User(0, "ghf@mail.ru", "dsdwe", "lex", LocalDate.of(3000, 9, 1));
        try {
            userController.add(user6);
        } catch (ValidationException e) {
            String message = e.getMessage();
            assertEquals(message, "дата рождения не может быть в будущем");
        }


    }
}