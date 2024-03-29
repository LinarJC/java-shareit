package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;

    @Autowired
    public ItemRequestController(ItemRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос к эндпоинту: '{} {}', Запрос на вещь: Описание: {}",
                "POST", "/requests",
                itemRequestDto.getDescription());
        return service.save(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoWithItems> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.findAll(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoWithItems> findByRequestId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "20") int size) {
        return service.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItems findByRequestId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PathVariable long requestId) {
        return service.findById(userId, requestId);
    }
}