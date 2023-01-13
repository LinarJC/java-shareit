package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findById(Long userId) {
        log.info("Запрошен метод поиска запроса по userId: {}", userId);
        return userMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("Пользователь с ID " + userId + " не найден")));
    }

    @Override
    public List<UserDto> findAll() {
        log.info("Запрошен метод поиска всех пользователей");
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto userDto) {
        log.info("Запрошен метод сохранения объекта user");
        log.info("Добавлен новый пользователь: '{}', ID '{}', '{}'", userDto.getName(), userDto.getId(), userDto.getEmail());
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        log.info("Запрошен метод обновления userId: {}", userId);
        UserDto oldUserDto = findById(userId);
        if (userDto.getName() != null) {
            oldUserDto.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            oldUserDto.setEmail(userDto.getEmail());
        }
        log.info("Внесены изменения в данные пользователя: '{}', ID '{}', '{}'",
                userDto.getName(), userDto.getId(), userDto.getEmail());
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(oldUserDto)));
    }

    @Override
    public void deleteById(Long userId) {
        log.info("Запрошен метод удаления userId: {}", userId);
        userRepository.deleteById(userId);
        log.info("Пользователь ID '{}' удален", userId);
    }
}