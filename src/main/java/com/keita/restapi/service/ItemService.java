package com.keita.restapi.service;

import java.util.List;

import com.keita.restapi.model.Item;

public interface ItemService {
    Item getItem(Long itemId);
    List<Item> getAllItems();
    Item getItemByName(String name);
}
