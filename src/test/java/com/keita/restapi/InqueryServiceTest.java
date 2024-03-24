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
        //Initializing list of inqueries and get all of it.
        List<Inquery> expectedInqueries = new ArrayList<>();
        expectedInqueries.add(new Inquery(1L, "Name1", "email1@example.com", "Message1", null, null));
        expectedInqueries.add(new Inquery(2L, "Name2", "email2@example.com", "Message2", null, null));

        when(inqueryRepositoryMock.findAll()).thenReturn(expectedInqueries);

        List<Inquery> actualInqueries = inqueryService.getAllInqueries();
        verify(inqueryRepositoryMock).findAll();

        //Assert expectedInqueries match the return list
        assertEquals(expectedInqueries, actualInqueries);
        //Assert expectedInqueries size is 2
        assertEquals(expectedInqueries.size(), 2);
    }

    @Test
    public void testGetInquery_ExistingId() {
        //Initializing list of inqueries and get all of it.
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
        //Initializing list of inqueries and get all of it.
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
