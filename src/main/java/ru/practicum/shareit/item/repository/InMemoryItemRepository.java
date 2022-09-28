package ru.practicum.shareit.item.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component
public class InMemoryItemRepository implements ItemRepository {
    private final InMemoryUserRepository userRepository;
    @Autowired
    private ItemMapper itemMapper;
    private final Map<Long, Item> items = new HashMap<>();
    private long id;

    @Override
    public Item addItem(Item item) {
        item.setId(++id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item removeItem(Long id) {
        return items.remove(id);
    }

    @Override
    public List<ItemDto> findAllItems() {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item: items.values()) {
            itemDtos.add(itemMapper.toItemDto(item));
        }
        log.info("Запрошены вещи: '{}'", itemDtos);
        return itemDtos;
    }

    @Override
    public ItemDto findItem(Long id) {
        Item item = items.getOrDefault(id, null);
        if (item == null) {
            throw new NotFoundException("Вещь с Id " + id + " не найден");
        }
        log.info("Запрошена вещь: '{}', ID '{}', '{}'", item.getId(), item.getName(), item.getDescription());
        return itemMapper.toItemDto(item);
    }

    public List<ItemDto> findAllItemsByUserId(Long userId) {
        if (userRepository.findUser(userId) == null) {
            throw new NotFoundException("Ползователь не найден");
        }
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, Predicate<Item> inName, Predicate<Item> inDescription) {
        return items.values()
                .stream()
                .filter(inName.or(inDescription))
                .filter(Item::getAvailable)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void validate(ItemDto itemDto, Long userId) {
        if (items.get(itemDto.getId()) == null) {
            throw new NotFoundException("Вещь с данным Id " + itemDto.getId() + " не найден");
        }
        if (userRepository.findUser(userId) == null) {
            throw new NotFoundException("Хозяин вещи не найден");
        }
        if (!items.get(itemDto.getId()).getOwner().getId().equals(userId)) {
            throw new NotFoundException("Id " + userId + " хозяина вещи не совпадает с указанным в параметрах");
        }
        log.info("Проведена валидация данных вещи: '{}'", itemDto);
    }

    @Override
    public boolean isExist(Long id) {
        return items.containsKey(id);
    }

    @Override
    public boolean isExist(Item item, User user) {
        return item.getOwner().equals(user);
    }
}
