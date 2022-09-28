package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final InMemoryItemRepository itemRepository;
    private final InMemoryUserRepository userRepository;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ItemDto getItem(Long itemId) {
        return itemRepository.findItem(itemId);
    }

    @Override
    public List<ItemDto> findAllItems() {
        return itemRepository.findAllItems();
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        Item item = itemRepository.addItem(itemMapper.toItem(itemDto, userMapper.toUser(userRepository.findUser(userId))));
        log.info("Добавлена новая вещь: '{}', ID '{}', '{}'", item.getId(), item.getName(), item.getDescription());
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long userId) {
        itemDto.setId(id);
        validate(itemDto, userId);
        if (itemRepository.findItem(itemDto.getId()) == null) {
            throw new NotFoundException("Вещь с данным Id " + itemDto.getId() + " не найден");
        }
        User owner = userMapper.toUser(userRepository.findUser(userId));
        Item item = itemMapper.toItem(itemDto, owner);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        } else {
            item.setName(itemRepository.findItem(id).getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        } else {
            item.setDescription(itemRepository.findItem(id).getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        } else {
            item.setAvailable(itemRepository.findItem(id).getAvailable());
        }
        if (itemDto.getRequest() != null) {
            item.setRequest(itemDto.getRequest());
        } else {
            item.setRequest(itemRepository.findItem(id).getRequest());
        }
        itemRepository.updateItem(item);
        log.info("Внесены изменения в данные вещи: '{}', ID '{}', '{}'",
                item.getName(), item.getId(), item.getDescription());
        return itemMapper.toItemDto(item);
    }

    @Override
    public void removeItem(Long id){
        itemRepository.removeItem(id);
        log.info("Вещь ID '{}' удалена", id);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        return  itemRepository.findAllItemsByUserId(userId);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (("").equals(text)) {
            return new ArrayList<>();
        }
        Predicate<Item> inName = item -> item.getName().toLowerCase().contains(text.toLowerCase());
        Predicate<Item> inDescription = item -> item.getDescription().toLowerCase().contains(text.toLowerCase());
        return itemRepository.search(text, inName, inDescription);
    }

    @Override
    public void validate(ItemDto itemDto, Long userId) {
        itemRepository.validate(itemDto, userId);
    }
}