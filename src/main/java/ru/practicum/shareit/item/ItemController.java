package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
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
    public List<ItemDtoWithBooking> findAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBooking findItemById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Вещь id {}", itemId);
        return itemService.findById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByText(@RequestParam String text) {
        log.info("Текст запроса на поиск = {}", text);
        return itemService.searchItem(text);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long id,
                          @RequestBody ItemDto itemDto) {
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
