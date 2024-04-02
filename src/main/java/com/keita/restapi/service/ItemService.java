package com.keita.restapi.service;

import java.util.List;

import com.keita.restapi.model.Item;

public interface ItemService {
    Item getItem(Long itemId);
    List<Item> getAllItems();
    Item getItemByName(String name);
    Item saveItem(Item item);
    Item updateItem(Long itemId, Item updatedItem);
    void deleteItem(Long itemId);
}
