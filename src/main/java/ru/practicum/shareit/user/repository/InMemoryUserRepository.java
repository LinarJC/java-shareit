package ru.practicum.shareit.user.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
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
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User findUser(Long userId) {
        return users.getOrDefault(userId, null);
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
