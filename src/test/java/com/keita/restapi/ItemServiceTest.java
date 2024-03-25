package com.keita.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public void getAllItems() {
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

}
