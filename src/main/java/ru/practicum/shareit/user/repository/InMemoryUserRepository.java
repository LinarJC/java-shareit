package ru.practicum.shareit.user.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@RequiredArgsConstructor
@Primary
@Slf4j
@Data
@Component
public class InMemoryUserRepository implements UserRepository {
    private Map<Long, User> users = new HashMap<>();
    private Set<String> emails = new HashSet<>();
    private long id;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User addUser(User user) {
        id++;
        users.put(id, user);
        emails.add(user.getEmail());
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        emails.remove(users.get(user.getId()).getEmail());
        emails.add(user.getEmail());
        return users.replace(user.getId(), user);
    }

    @Override
    public User removeUser(User user) {
        emails.remove(user.getEmail());
        return users.remove(user.getId());
    }

    @Override
    public void removeAll() {
        emails.clear();
        users.clear();
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserDto> userDtos = new ArrayList<>();
            for (User user: users.values()) {
                userDtos.add(userMapper.toUserDto(user));
            }
        return userDtos;
    }

    @Override
    public UserDto findUser(Long userId) {
        if (users.get(userId) == null) {
            throw new NotFoundException("Пользователь с данным Id не найден");
        }
        return userMapper.toUserDto(users.get(userId));
    }

    @Override
    public boolean isExist(User user) {
        return users.containsKey(user.getId());
    }

    @Override
    public boolean isExistEmail(String email) {
        return emails.contains(email);
    }
}
