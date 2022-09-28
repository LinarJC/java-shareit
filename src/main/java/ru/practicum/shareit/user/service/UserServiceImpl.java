package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final InMemoryUserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserDto getUser(Long userId) {
        return userRepository.findUser(userId);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public UserDto createUser(UserDto userDto) {
        validate(userDto);
        userDto.setId(userRepository.getId() + 1);
        userRepository.addUser(userMapper.toUser(userDto));
        log.info("Добавлен новый пользователь: '{}', ID '{}', '{}'", userDto.getName(), userDto.getId(), userDto.getEmail());
        return userDto;
    }

    public UserDto updateUser(UserDto userDto, Long id) {
        userDto.setId(id);
        if (!userRepository.isExist(userMapper.toUser(userDto))) {
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
        userRepository.updateUser(userMapper.toUser(userDto));
        log.info("Внесены изменения в данные пользователя: '{}', ID '{}', '{}'",
                userDto.getName(), userDto.getId(), userDto.getEmail());
        return userDto;
    }

    public void removeUser(Long id) {
        userRepository.removeUser(userMapper.toUser(userRepository.findUser(id)));
        log.info("Пользователь ID '{}' удален", id);
    }

    public void validate(UserDto userDto) {
        if (userRepository.isExistEmail(userDto.getEmail())) {
            throw new DuplicateEmailFoundException("Пользователь с данным E-mail уже есть в базе.");
        }
        log.info("Проведена валидация данных пользователя: '{}'", userDto);
    }
}