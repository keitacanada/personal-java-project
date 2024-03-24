package com.keita.restapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keita.restapi.exception.ItemNotFoundException;
import com.keita.restapi.model.Item;
import com.keita.restapi.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Override
    public Item getItem(Long itemId) {
        Optional<Item> potentialItem = itemRepository.findById(itemId);
        if(!potentialItem.isPresent()) {
            throw new ItemNotFoundException("Item with ID " + itemId + " isn't found");
        }

        return potentialItem.get();
    }

    @Override
    public List<Item> getAllItems() {
        return (List<Item>) itemRepository.findAll();
    }
}
