package com.keita.restapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keita.restapi.model.Item;
import com.keita.restapi.service.ItemService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/item")
public class itemController {

    @Autowired
    ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable Long itemId) {
        // Get an item by item id
        return new ResponseEntity<>(itemService.getItem(itemId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        // Create a new item
        return new ResponseEntity<>(itemService.saveItem(item), HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable Long itemId, @Valid @RequestBody Item item) {
        // Update an item
        return new ResponseEntity<>(itemService.updateItem(itemId, item), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long itemId) {
        // Delete an item by ID
        itemService.deleteItem(itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
