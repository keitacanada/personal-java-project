package com.keita.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.keita.restapi.exception.InqueryNotFoundException;
import com.keita.restapi.model.Inquery;
import com.keita.restapi.model.Item;
import com.keita.restapi.repository.InqueryRepository;
import com.keita.restapi.service.InqueryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class InqueryServiceTest {

    @Mock
    private InqueryRepository inqueryRepositoryMock;

    @InjectMocks
    private InqueryServiceImpl inqueryService;



    @Test
    public void testGetAllInqueries() {
        //Initializing list of inqueries and get all of it
        List<Inquery> expectedInqueries = new ArrayList<>();
        expectedInqueries.add(new Inquery(1L, "Name1", "email1@example.com", "Message1", null, null));
        expectedInqueries.add(new Inquery(2L, "Name2", "email2@example.com", "Message2", null, null));

        when(inqueryRepositoryMock.findAll()).thenReturn(expectedInqueries);

        List<Inquery> actualInqueries = inqueryService.getAllInqueries();
        verify(inqueryRepositoryMock).findAll();

        //Assert expectedInqueries match the return list
        assertEquals(expectedInqueries, actualInqueries);
        //Assert expectedInqueries size is 2
        assertEquals(2, expectedInqueries.size());
    }

    @Test
    public void getInqueriesByItemId() {
       // create a sample item object
       Long sampleItemId = 1L;
       Item sampleItem = new Item(sampleItemId, "Test item", null, "This is description", null, null);

       // create list of inqueries
       List<Inquery> expectedInqueries = new ArrayList<>();
       expectedInqueries.add(new Inquery(1L, "Name1", "email1@example.com", "Message1", null, sampleItem));
       expectedInqueries.add(new Inquery(2L, "Name2", "email2@example.com", "Message2", null, sampleItem));

       when(inqueryRepositoryMock.findAllByItemId(sampleItemId)).thenReturn(expectedInqueries);

       List<Inquery> actualInqueries = inqueryService.getInqueriesByItemId(sampleItemId);

       int expectedInqueriesSize = 2;
       assertEquals(expectedInqueriesSize, actualInqueries.size());

       assertEquals(expectedInqueries.size(), actualInqueries.size());
       for (int i = 0; i < actualInqueries.size(); i++) {
           assertEquals(expectedInqueries.get(i).getName(), actualInqueries.get(i).getName());
           assertEquals(expectedInqueries.get(i).getEmail(), actualInqueries.get(i).getEmail());
           assertEquals(expectedInqueries.get(i).getMessage(), actualInqueries.get(i).getMessage());
           assertEquals(expectedInqueries.get(i).getItem().getId(), actualInqueries.get(i).getItem().getId());
           assertEquals(expectedInqueries.get(i).getItem().getName(), actualInqueries.get(i).getItem().getName());
       }
    }

    @Test
    public void getInqueriesByItemId_NonExistingItemId() {
        // This item id doesn't exist
        Long nonExistingItemId = 100L;

        when(inqueryRepositoryMock.findAllByItemId(nonExistingItemId)).thenReturn(Collections.emptyList());
        // assert inquries with the item id don't exist
        assertThrows(InqueryNotFoundException.class, () -> {
            inqueryService.getInqueriesByItemId(nonExistingItemId);
        });

        verify(inqueryRepositoryMock).findAllByItemId(nonExistingItemId);
    }

    @Test
    public void testGetInquery_ExistingId() {
        //Initializing list of inqueries and get all of it
        List<Inquery> expectedInqueries = new ArrayList<>();
        expectedInqueries.add(new Inquery(1L, "Name1", "email1@example.com", "Message1", null, null));
        expectedInqueries.add(new Inquery(2L, "Name2", "email2@example.com", "Message2", null, null));

        Inquery expectedInquery = expectedInqueries.get(0);
        when(inqueryRepositoryMock.findById(1L)).thenReturn(Optional.of(expectedInquery));

        // Call the service
        Inquery actualInquery = inqueryService.getInquery(1L);

        // Verify that repository was called by correct id
        verify(inqueryRepositoryMock).findById(1L);

        // Assert that expected inquery matches actual inquery
        assertEquals(expectedInquery, actualInquery);
    }

    @Test
    public void testGetInquery_NonExistingId() {
        //Creating mock Inquery objects
        List<Inquery> expectedInqueries = new ArrayList<>();
        expectedInqueries.add(new Inquery(1L, "Name1", "email1@example.com", "Message1", null, null));
        expectedInqueries.add(new Inquery(2L, "Name2", "email2@example.com", "Message2", null, null));

        // call non-existing id inquery object
        when(inqueryRepositoryMock.findById(3L)).thenReturn(Optional.empty());

        // Call the service and expect an custom exception
        assertThrows(InqueryNotFoundException.class, () -> {
            inqueryService.getInquery(3L);
        });

        // Verify that the repository method was called with the correct ID
        verify(inqueryRepositoryMock).findById(3L);
    }


}
