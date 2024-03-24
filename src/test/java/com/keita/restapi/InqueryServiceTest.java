package com.keita.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
