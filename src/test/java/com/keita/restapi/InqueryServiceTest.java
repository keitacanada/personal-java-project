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

import com.keita.restapi.inquery.Inquery;
import com.keita.restapi.inquery.InqueryNotFoundException;
import com.keita.restapi.inquery.InqueryRepository;
import com.keita.restapi.inquery.InqueryServiceImpl;
import com.keita.restapi.item.Item;
import com.keita.restapi.user.Role;
import com.keita.restapi.user.User;

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
        expectedInqueries.add(new Inquery(1L, "Message1", null, null, null));
        expectedInqueries.add(new Inquery(2L, "Message2", null, null, null));

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

       // create 2 sample users;
       User sampleUser1 = createUser(1L, "user1", "user1@example.com", "password", "USER");
       User sampleUser2 = createUser(2L, "user2", "user2@example.com", "password", "USER");

       // create list of inqueries
       List<Inquery> expectedInqueries = new ArrayList<>();
       expectedInqueries.add(new Inquery(1L, "Message1", null, sampleItem, sampleUser1));
       expectedInqueries.add(new Inquery(1L, "Message1", null, sampleItem, sampleUser2));

       when(inqueryRepositoryMock.findAllByItemId(sampleItemId)).thenReturn(expectedInqueries);

       List<Inquery> actualInqueries = inqueryService.getInqueriesByItemId(sampleItemId);

       int expectedInqueriesSize = 2;
       assertEquals(expectedInqueriesSize, actualInqueries.size());

       assertEquals(expectedInqueries.size(), actualInqueries.size());
       for (int i = 0; i < actualInqueries.size(); i++) {
           assertEquals(expectedInqueries.get(i).getMessage(), actualInqueries.get(i).getMessage());
           assertEquals(expectedInqueries.get(i).getMessage(), actualInqueries.get(i).getMessage());
           assertEquals(expectedInqueries.get(i).getItem().getId(), actualInqueries.get(i).getItem().getId());
           assertEquals(expectedInqueries.get(i).getItem().getName(), actualInqueries.get(i).getItem().getName());
           assertEquals(expectedInqueries.get(i).getUser().getId(), actualInqueries.get(i).getUser().getId());
           assertEquals(expectedInqueries.get(i).getUser().getUsername(), actualInqueries.get(i).getUser().getUsername());
       }
    }

    /**
     * Method to create a sample user
     *
     * @param id
     * @param username
     * @param email
     * @param password
     * @param role
     * @return User object
    */

    private User createUser(Long id, String username, String email, String password, String role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(Role.valueOf(role));

        return user;
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
        expectedInqueries.add(new Inquery(1L, "Message1", null, null, null));
        expectedInqueries.add(new Inquery(2L, "Message2", null, null, null));

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
        expectedInqueries.add(new Inquery(1L, "Message1", null, null, null));
        expectedInqueries.add(new Inquery(2L, "Message2", null, null, null));

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
