package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User addUser(User user);

    User updateUser(User user);

    User removeUser(User user);

    void removeAll();

    List<UserDto> findAllUsers();

    UserDto findUser(Long userId);

    boolean isExist(User user);

    boolean isExistEmail(String email);
}
