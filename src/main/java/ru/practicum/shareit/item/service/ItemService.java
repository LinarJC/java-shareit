package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {

    ItemDtoWithBooking findById(Long itemId, Long userId);

    List<ItemDtoWithBooking> findAll(Long userId);

    ItemDto save(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long userId, Long id);

    void deleteById(Long itemId);

    List<ItemDto> searchItem(String text);

    CommentDto saveComment(Long userId, Long itemId, CommentDto commentDto);
}
