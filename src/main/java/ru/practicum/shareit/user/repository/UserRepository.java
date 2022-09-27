package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {

    User addUser(User user);

    User updateUser(User user);

    User removeUser(User user);

    void removeAll();

    Collection<User> findAllUsers();

    User findUser(Long userId);

    boolean isExist(User user);

    boolean isExistEmail(String email);
}
