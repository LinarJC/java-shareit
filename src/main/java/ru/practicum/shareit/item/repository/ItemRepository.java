package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Item item);

    Item removeItem(Item item);

    Collection<Item> findAllItems();

    Item findItem(Long id);


    boolean isExist(Long id);
}
