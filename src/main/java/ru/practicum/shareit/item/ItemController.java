package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }
    @PostMapping
    public ItemDto addItem( @RequestHeader("X-Sharer-User-Id") Long userId,
                            @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @GetMapping("/{id}")
    public ItemDto findItemById(@PathVariable Long id) {
        return itemService.getItem(id);
    }

    @GetMapping("search")
    public List<ItemDto> search(@RequestParam(required = false) String text) {
        log.info("search text={}", text);
        return itemService.search(text);
    }

    @GetMapping
    public List<ItemDto> findAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable Long id,
                              @RequestHeader("X-Sharer-User-Id") Long userId,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto) {
        return itemService.updateItem(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        itemService.removeItem(id);
    }
}
