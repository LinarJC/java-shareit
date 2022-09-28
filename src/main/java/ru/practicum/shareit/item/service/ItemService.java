package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto getItem(Long itemId);

    Collection<ItemDto> findAllItems();

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(Long id, ItemDto itemDto, Long userId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> search(String text);

    void validate(ItemDto itemDto, Long userId);

    void removeItem(Long id);
}
