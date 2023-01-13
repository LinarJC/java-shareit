package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос к эндпоинту: '{} {}', Вещь: Наименование: {} и Описание: {}", "POST", "/items",
                itemDto.getName(), itemDto.getDescription());
        return itemService.save(itemDto, userId);
    }

    @GetMapping
    public List<ItemDtoWithBooking> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(defaultValue = "0") @Min(0) int from,
                                            @RequestParam(defaultValue = "20") @Positive int size) {
        return itemService.findAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBooking findItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Вещь id {}", itemId);
        return itemService.findById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByText(@RequestParam String text,
                                        @RequestParam(defaultValue = "0") @Min(0) int from,
                                        @RequestParam(defaultValue = "20") @Positive int size) {
        log.info("Текст запроса на поиск = {}", text);
        return itemService.searchItem(text, from, size);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long id,
                          @RequestBody ItemDto itemDto) {
        log.info("PATCH user id={}, item id={}", userId, id);
        return itemService.update(itemDto, userId, id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody CommentDto commentDto,
                                    @PathVariable long itemId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Вещь с Id: {}, Комментарий: {}",
                "POST", "/items/{itemId}/comment",
                itemId, commentDto.getText());
        return itemService.saveComment(userId, itemId, commentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable long id) {
        log.info("Удалена вещь id {}", id);
        itemService.deleteById(id);
    }
}
