package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final InMemoryUserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = (InMemoryUserRepository) userRepository;
    }

    public UserDto getUser(Long userId) {
        User user = userRepository.findUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с данным Id не найден");
        }
        return toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user: userRepository.findAllUsers()) {
            userDtos.add(toUserDto(user));
        }
        return userDtos;
    }

    public UserDto createUser(UserDto userDto) {
        validate(userDto);
        userDto.setId(userRepository.getId() + 1);
        userRepository.addUser(UserMapper.toUser(userDto));
        log.info("Добавлен новый пользователь: '{}', ID '{}', '{}'", userDto.getName(), userDto.getId(), userDto.getEmail());
        return userDto;
    }

    public UserDto updateUser(UserDto userDto, Long id) {
        userDto.setId(id);
        if (!userRepository.isExist(UserMapper.toUser(userDto))) {
            throw new NotFoundException("Пользователь с данным Id " + id + " не найден");
        }
        if (userDto.getName() == null) {
            userDto.setName(userRepository.findUser(id).getName());
        }
        if (userDto.getEmail() == null) {
            userDto.setEmail(userRepository.findUser(id).getEmail());
        } else {
            validate(userDto);
        }
        userRepository.updateUser(UserMapper.toUser(userDto));
        log.info("Внесены изменения в данные пользователя: '{}', ID '{}', '{}'",
                userDto.getName(), userDto.getId(), userDto.getEmail());
        return userDto;
    }

    public void removeUser(Long id) {
        userRepository.removeUser(userRepository.findUser(id));
        log.info("Пользователь ID '{}' удален", id);
    }

    public void validate(UserDto userDto) {
        if (userRepository.isExistEmail(userDto.getEmail())) {
            throw new DuplicateEmailFoundException("Пользователь с данным E-mail уже есть в базе.");
        }
        log.info("Проведена валидация данных пользователя: '{}'", userDto);
    }
}