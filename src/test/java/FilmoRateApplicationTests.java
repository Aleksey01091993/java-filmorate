import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.impl.FriendsDaoImpl;
import ru.yandex.practicum.filmorate.storage.dao.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final FriendsDaoImpl friendsDao;

    @Test
    public void testUserAddGetId() throws DataNotFoundException {
        User user = new User("mr@ta.ru", "hjkj", "nam", LocalDate.now());
        User user1 = userStorage.add(user);
        user1.setId(1);
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
    public void testUpdateUser() throws DataNotFoundException {
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
    public void testFriendsTest() throws DataNotFoundException {
        User user = userStorage.add(new User("1mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user1 = userStorage.add(new User("2mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        User user2 = userStorage.add(new User("3mr@ta.ru", "hjkj", "nam", LocalDate.now()));
        user.setId(1);
        user1.setId(2);
        user2.setId(3);
        int userId = 1;
        int userId1 = 2;
        int userId2 = 3;
        friendsDao.addFriend(userId, userId1);
        friendsDao.addFriend(userId1, userId);
        Assertions.assertEquals(user1, friendsDao.friends(userId).get(0));
        Assertions.assertEquals(user, friendsDao.friends(userId1).get(0));
        friendsDao.addFriend(userId, userId2);
        friendsDao.addFriend(userId1, userId2);
        Assertions.assertEquals(user2, friendsDao.friends(userId).get(1));
        Assertions.assertEquals(user2, friendsDao.friends(userId1).get(1));
        Assertions.assertEquals(friendsDao.mutualFriends(userId, userId1).get(0), user2);
        friendsDao.deleteFriend(userId, userId2);
        friendsDao.deleteFriend(userId1, userId2);
        Assertions.assertTrue(friendsDao.mutualFriends(userId, userId1).isEmpty());
    }
/*
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

     */
}