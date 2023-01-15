package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {

    ItemDtoWithBooking findById(Long itemId, long userId);

    List<ItemDtoWithBooking> findAll(Long userId, int from, int size);

    ItemDto save(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, long userId, Long id);

    void deleteById(Long itemId);

    List<ItemDto> searchItem(String text, int from, int size);

    CommentDto saveComment(Long userId, Long itemId, CommentDto commentDto);
}
