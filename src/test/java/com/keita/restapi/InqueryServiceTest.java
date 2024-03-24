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

    @Test
    public void testGetInqueryByItemId_Existing() {
        // Create a mock Item object
        Item item = new Item(1L, "Shoes", null, "This is a pair of shoes", null);

        // Create a mock Inquery object with the Item object above
        Inquery expectedInquery = new Inquery(1L, "Name1", "email1@example.com", "Message1", null, item);

        // Mock behavior of the repository to return the expected Inquery object
        when(inqueryRepositoryMock.findByItemId(1L)).thenReturn(Optional.of(expectedInquery));

        // Call inqueryService.
        Inquery actualInquery = inqueryService.getInqueryByItemId(1L);

        // Verify response with item id
        verify(inqueryRepositoryMock).findByItemId(1L);

        // Assert that the returned Inquery object matches expected one
        assertEquals(expectedInquery, actualInquery);
    }

    @Test
    public void testGetInqueryByItemId_NonExisting() {
        // Mock the behavior of the repository to return an empty optional
        when(inqueryRepositoryMock.findByItemId(2L)).thenReturn(Optional.empty());

        // Call the service and expect an exception
        assertThrows(InqueryNotFoundException.class, () -> {
            inqueryService.getInqueryByItemId(2L);
        });

        // Verify that the repository was called with the correct item ID
        verify(inqueryRepositoryMock).findByItemId(2L);
    }
}
