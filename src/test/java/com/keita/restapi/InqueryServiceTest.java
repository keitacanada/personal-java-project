package com.keita.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.keita.restapi.authentication.AuthService;
import com.keita.restapi.inquery.Inquery;
import com.keita.restapi.inquery.InqueryNotFoundException;
import com.keita.restapi.inquery.InqueryRepository;
import com.keita.restapi.inquery.InqueryService;
import com.keita.restapi.inquery.InqueryServiceImpl;
import com.keita.restapi.item.Item;
import com.keita.restapi.item.ItemRepository;
import com.keita.restapi.item.ItemServiceImpl;
import com.keita.restapi.user.Role;
import com.keita.restapi.user.User;
import com.keita.restapi.user.UserRepository;

import jakarta.persistence.criteria.CriteriaBuilder.In;

@ExtendWith(MockitoExtension.class)
public class InqueryServiceTest {

    @Mock
    private InqueryRepository inqueryRepositoryMock;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private AuthService authServiceMock;

    @InjectMocks
    private InqueryServiceImpl inqueryService;

    @InjectMocks
    private ItemServiceImpl itemService;

    // Verifies that all the inquiries are retrieved successfully
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
    // Verifies that all inqueries for a specific item id are retrieved successfully
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


    // Verifies that inquiries for a non-existing item id generates InquiryNotFoundException
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

    // Verifies that an inquiry with an id is retrieved successfully
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

    // Verifies that an inquiry with a non-exisiting id generates InqueryNotFoundException
    @Test
    public void testGetInquery_NonExistingId() {
        //Creating mock Inquiry objects
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

    // Verifies that an inquiry for an item from user is posted successfully
    @Test
    public void testSaveInquery_successful() {
        // Creating mock item data
        Long itemId = 1L;

        // Creating item and user objects
        Item item = new Item(itemId, "sameItem", null,  "Description", null, null);

        // Mocking principal
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        // Mocking repository method calls
        when(itemRepositoryMock.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepositoryMock.findByUsername("testUser")).thenReturn(Optional.of(new User()));

        // Creating the inquery to be saved
        Inquery inquery = new Inquery();
        inquery.setMessage("New inquiry message");

        // Saving the inquery
        inqueryService.saveInquery(inquery, itemId);

        // Verifying repository method calls
        verify(itemRepositoryMock).findById(itemId);
        verify(userRepositoryMock).findByUsername("testUser");
        verify(inqueryRepositoryMock).save(inquery);
    }

    // Verifies that an inquiry for an item from user TWICE generates RUNTIME Exception.
    @Test
    public void testSaveInquery_sameInqueryTwice() {
        // Creating mock item data
        Long itemId = 1L;

        // Creating item and user objects
        Item item = new Item(itemId, "sameItem", null, "Description", null, null);
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        // Mocking principal
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user1.getUsername());

        // Mocking repository method calls
        when(itemRepositoryMock.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepositoryMock.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        // Mocking inqueryRepository to return an existing inquery for user1 and itemA
        when(inqueryRepositoryMock.findByItemIdAndUserId(itemId, user1.getId())).thenReturn(Optional.of(new Inquery()));

        // Creating the inquery to be saved
        Inquery inquery = new Inquery();
        inquery.setMessage("New inquery message");

        // Saving the inquery and expecting an exception
        assertThrows(RuntimeException.class, () -> inqueryService.saveInquery(inquery, itemId));

        // Verifying repository method calls
        verify(itemRepositoryMock).findById(itemId);
        verify(userRepositoryMock).findByUsername(user1.getUsername());
        verify(inqueryRepositoryMock).findByItemIdAndUserId(itemId, user1.getId());
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

}


