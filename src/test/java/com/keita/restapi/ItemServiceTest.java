package com.keita.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.keita.restapi.exception.ItemNotFoundException;
import com.keita.restapi.model.Item;
import com.keita.restapi.repository.ItemRepository;
import com.keita.restapi.service.ItemServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepositoryMock;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void TestGetAllItems() {
        // Create Item objects and insert into arraylist
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "shoes", null, "Good condition", null));
        items.add(new Item(2L, "shirts", null, "nice condition", null));

        when(itemRepositoryMock.findAll()).thenReturn(items);

        List<Item> actualItems = itemService.getAllItems();
        // Assert Items match the actualItems
        assertEquals(items, actualItems);
        // Assert there are 2 items
        assertEquals(actualItems.size(), 2);
    }

    @Test
    public void testGetItem_ExistingId() {
        // Create Item objects and insert into arraylist
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "shoes", null, "Good condition", null));
        items.add(new Item(2L, "shirts", null, "nice condition", null));

        Item expectedItem = items.get(0);

        when(itemRepositoryMock.findById(1L)).thenReturn(Optional.of(expectedItem));
        Item actualItem = itemService.getItem(1L);
        verify(itemRepositoryMock).findById(1L);

        assertEquals(expectedItem, actualItem);
    }

    @Test
    public void testGetItem_NonExistingId() {
        // Create Item objects and insert into arraylist
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "shoes", null, "Good condition", null));
        items.add(new Item(2L, "shirts", null, "nice condition", null));

        // call non-existing id of Item object
        when(itemRepositoryMock.findById(3L)).thenReturn(Optional.empty());

        // Call the service and expect a custom exception
        assertThrows(ItemNotFoundException.class, () -> {
            itemService.getItem(3L);
        });

    }


}
