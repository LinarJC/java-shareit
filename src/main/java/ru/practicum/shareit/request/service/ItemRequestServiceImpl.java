package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper mapper;
    private final UserRepository userRepository;


    @Override
    public ItemRequestDto save(ItemRequestDto itemRequestDto, long userId) {
        log.info("Запрошен метод save для: {}", userId);
        ItemRequest itemRequest = mapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userRepository.findById(userId)
                .orElseThrow(() -> new StorageException("Incorrect userId")));
        return mapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDtoWithItems> findAll(long userId) {
        log.info("Запрошен метод поиска всех запросов по userId: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new StorageException("Пользователя с Id = " + userId + " нет в БД");
        }
        return itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(mapper::toItemRequestDtoWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoWithItems findById(long userId, long itemRequestId) {
        log.info("Запрошен метод поиска запроса по userId: {} и itemRequestId: {}", userId, itemRequestId);
        if (!userRepository.existsById(userId)) {
            throw new StorageException("Пользователя с Id = " + userId + " нет в БД");
        }
        ItemRequest itemRequest = itemRequestRepository
                .findById(itemRequestId).orElseThrow(() ->
                        new StorageException("Запроса с Id = " + itemRequestId + " нет в БД"));
        return mapper.toItemRequestDtoWithItems(itemRequest);
    }

    @Override
    public List<ItemRequestDtoWithItems> findAll(long userId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("created"));
        if (!userRepository.existsById(userId)) {
            throw new StorageException("Пользователя с Id = " + userId + " нет в БД");
        }
        return itemRequestRepository.findAll(pageable)
                .stream()
                .filter(itemRequest -> itemRequest.getRequestor().getId() != userId)
                .map(mapper::toItemRequestDtoWithItems)
                .collect(Collectors.toList());
    }
}