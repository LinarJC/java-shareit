package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto save(ItemRequestDto itemRequestDto, long userId);

    List<ItemRequestDtoWithItems> findAll(long userId);

    ItemRequestDtoWithItems findById(long userId, long itemRequestId);

    List<ItemRequestDtoWithItems> findAllWithPageable(long userId, int from, int size);
}