package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService{
    private final InMemoryItemRepository itemRepository;
    private final InMemoryUserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = (InMemoryItemRepository) itemRepository;
        this.userRepository = (InMemoryUserRepository) userRepository;
    }

    @Override
    public ItemDto getItem(Long itemId) {
        Item item = itemRepository.findItem(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с данным Id " + itemId + " не найден");
        }
        log.info("Запрошена вещь: '{}', ID '{}', '{}'", item.getId(), item.getName(), item.getDescription());
        return toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllItems() {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item: itemRepository.findAllItems()) {
            itemDtos.add(toItemDto(item));
        }
        return itemDtos;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        if (userRepository.findUser(userId) == null) {
            throw new NotFoundException("Пользователь с данным Id не найден");
        }
        Item item = itemRepository.addItem(ItemMapper.toItem(itemDto, userRepository.findUser(userId)));
        log.info("Добавлена новая вещь: '{}', ID '{}', '{}'", item.getId(), item.getName(), item.getDescription());
        return toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long userId) {
        itemDto.setId(id);
        validate(itemDto, userId);
        User owner = userRepository.findUser(userId);
        Item item = ItemMapper.toItem(itemDto, owner);
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
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void removeItem(Long id){
        itemRepository.removeItem(itemRepository.findItem(id));
        log.info("Вещь ID '{}' удалена", id);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        if (userRepository.findUser(userId) == null) {
            throw new NotFoundException("Ползователь не найден");
        }
        return itemRepository.findAllItems()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (("").equals(text)) {
            return new ArrayList<>();
        }
        Predicate<Item> inName = item -> item.getName().toLowerCase().contains(text.toLowerCase());
        Predicate<Item> inDescription = item -> item.getDescription().toLowerCase().contains(text.toLowerCase());
        return itemRepository.findAllItems()
                .stream()
                .filter(inName.or(inDescription))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void validate(ItemDto itemDto, Long userId) {
        if (itemRepository.findItem(itemDto.getId()) == null) {
            throw new NotFoundException("Вещь с данным Id " + itemDto.getId() + " не найден");
        }
        if (userRepository.findUser(userId) == null) {
            throw new NotFoundException("Хозяин вещи не найден");
        }
        if (!itemRepository.findItem(itemDto.getId()).getOwner().getId().equals(userId)) {
            throw new NotFoundException("Id "  + userId + " хозяина вещи не совпадает с указанным в параметрах");
        }
        log.info("Проведена валидация данных вещи: '{}'", itemDto);
    }
}