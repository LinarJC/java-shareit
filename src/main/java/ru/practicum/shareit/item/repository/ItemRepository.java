package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Item item);

    Item removeItem(Long id);

    Collection<ItemDto> findAllItems();

    ItemDto findItem(Long id);

    List<ItemDto> findAllItemsByUserId(Long userId);

    List<ItemDto> search(String text, Predicate<Item> inName, Predicate<Item> inDescription);

    boolean isExist(Long id);

    boolean isExist(Item item, User user);

    void validate(ItemDto itemDto, Long userId);
}
