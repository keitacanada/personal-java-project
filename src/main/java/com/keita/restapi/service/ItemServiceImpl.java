package com.keita.restapi.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keita.restapi.exception.ItemNotFoundException;
import com.keita.restapi.exception.PhotoFormatException;
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

    @Override
    public Item getItemByName(String name) {
        Optional<Item> potentialItem = itemRepository.findByName(name);
    if (!potentialItem.isPresent()) {
        throw new ItemNotFoundException("Item with name " + name + " isn't found");
    }
    return potentialItem.get();
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long itemId, Item updatedItem) {
        Optional<Item> potentialItem = itemRepository.findById(itemId);
        if (!potentialItem.isPresent()) {
            throw new ItemNotFoundException("Item with ID " + itemId + " isn't found");
        }

        Item existingItem = potentialItem.get();
        existingItem.setName(updatedItem.getName());
        existingItem.setImage(updatedItem.getImage());
        existingItem.setDescription(updatedItem.getDescription());

        return itemRepository.save(existingItem);
    }

    @Override
    public void deleteItem(Long itemId) {
        Optional<Item> potentialItem = itemRepository.findById(itemId);
        if (!potentialItem.isPresent()) {
            throw new ItemNotFoundException("Item with ID " + itemId + " isn't found");
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    public Item createItemWithImage(Item item, MultipartFile imageFile) {
        try {
            if(imageFile != null) {
                item.setImage(imageFile.getBytes());
            }
            return itemRepository.save(item);
        } catch(IOException e) {
            throw new PhotoFormatException("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            throw new PhotoFormatException("Failed to create item: " + e.getMessage());
        }
    }



}
