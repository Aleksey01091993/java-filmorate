import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testUserAddGetId() {
        User user = new User("mr@ta.ru", "hjkj", "nam", LocalDate.now());
        User user1 = userStorage.add(user);
        User user2 = userStorage.getUser(1);
        Assertions.assertEquals(user1, user2);

    }

    @Test
    public void testGetUsers() {
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        List<User> users = userStorage.getUsers();
        Assertions.assertEquals(users.size(), 3);
    }

    @Test
    public void testUpdateUser() {
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        userStorage.add(new User("mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user = new User("mraa@ta.ru", "lex", "lkj", LocalDate.now());
        user.setId(1);
        userStorage.update(user);
        User user1 = userStorage.getUser(1);
        Assertions.assertEquals(user1, user);
    }

    @Test
    public void testFriendsTest() {
        User user = userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user1 = userStorage.add(new User("2mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user2 = userStorage.add(new User("3mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        int userId = user.getId();
        int userId1 = user1.getId();
        int userId2 = user2.getId();
        userStorage.addFriend(userId, userId1);
        userStorage.addFriend(userId1, userId);
        Assertions.assertEquals(user1, userStorage.friends(userId).get(0));
        Assertions.assertEquals(user, userStorage.friends(userId1).get(0));
        userStorage.addFriend(userId, userId2);
        userStorage.addFriend(userId1, userId2);
        Assertions.assertEquals(user2, userStorage.friends(userId).get(1));
        Assertions.assertEquals(user2, userStorage.friends(userId1).get(1));
        Assertions.assertEquals(userStorage.mutualFriends(userId, userId1).get(0), user2);
        userStorage.deleteFriend(userId, userId2);
        userStorage.deleteFriend(userId1, userId2);
        System.out.println(userStorage.mutualFriends(userId, userId1));
        Assertions.assertTrue(userStorage.mutualFriends(userId, userId1).isEmpty());
    }

    @Test
    public void testFilmAddGetId() {
        List<Integer> genre = new ArrayList<>();
        genre.add(1);
        genre.add(2);
        genre.add(3);
        Film film = new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre);
        Film film1 = filmDbStorage.add(film);
        Film film2 = filmDbStorage.getFilm(film1.getId());
        Assertions.assertEquals(film1, film2);

    }

    @Test
    public void testGetFilms() {
        List<Integer> genre = new ArrayList<>();
        genre.add(1);
        genre.add(2);
        genre.add(3);
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre));
        List<Film> films = filmDbStorage.getFilms();
        Assertions.assertEquals(films.size(), 3);
    }

    @Test
    public void testUpdateFilm() {
        List<Integer> genre = new ArrayList<>();
        genre.add(1);
        genre.add(2);
        genre.add(3);
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre));
        filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre));
        Film film = new Film("fiewdwelm", "oiuofrefiu", LocalDate.now(), 5.56F, 2, genre);
        film.setId(1);
        filmDbStorage.update(film);
        Film film1 = filmDbStorage.getFilm(1);
        System.out.println(film1);
        System.out.println(film);
        Assertions.assertEquals(film, film1);
    }

    @Test
    public void testAddDeleteLikeTopFilm() {
        List<Integer> genre = new ArrayList<>();
        genre.add(1);
        genre.add(2);
        genre.add(3);
        int userId = userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now())).getId();
        int userId1 = userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now())).getId();
        int userId2 = userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now())).getId();
        int filmId = filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre)).getId();
        int filmId1 = filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre)).getId();
        int filmId2 = filmDbStorage.add(new Film("film", "oiuoiu", LocalDate.now(), 4.56F, 1, genre)).getId();
        filmDbStorage.addLike(filmId, userId);
        filmDbStorage.addLike(filmId1, userId);
        filmDbStorage.addLike(filmId1, userId2);
        filmDbStorage.addLike(filmId2, userId);
        filmDbStorage.addLike(filmId2, userId1);
        filmDbStorage.addLike(filmId2, userId2);
        List<Film> topFilms = filmDbStorage.topFilms(3);

    }
}