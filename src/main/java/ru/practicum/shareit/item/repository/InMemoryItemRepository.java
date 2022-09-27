package ru.practicum.shareit.item.repository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long id;
    @Override
    public Item addItem(Item item) {
        item.setId(++id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item removeItem(Item item) {
        return items.remove(item.getId());
    }

    @Override
    public Collection<Item> findAllItems() {
        return items.values();
    }

    @Override
    public Item findItem(Long id) {
        return items.getOrDefault(id, null);
    }

    public boolean isExist(Long id) {
        return items.containsKey(id);
    }

    public boolean isExist(Item item, User user) {
        return item.getOwner().equals(user);
    }
}
