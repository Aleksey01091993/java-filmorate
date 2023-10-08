package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;

import java.util.List;

@Service
public class FriendsService {

    private final FriendsDao friends;

    public FriendsService(@Autowired @Qualifier("FriendsDaoImpl") FriendsDao friends) {
        this.friends = friends;
    }

    public void addFriend(int userId, int friendId) {
        friends.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        friends.deleteFriend(userId, friendId);
    }

    public List<User> friends(long userId) {
        return friends.friends(userId);
    }

    public List<User> mutualFriends(long userId, long friendId) throws DataNotFoundException {
        return friends.mutualFriends(userId, friendId);
    }

}
