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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.keita.restapi.authentication.AuthService;
import com.keita.restapi.inquiry.Inquiry;
import com.keita.restapi.inquiry.InquiryNotFoundException;
import com.keita.restapi.inquiry.InquiryRepository;
import com.keita.restapi.inquiry.InquiryServiceImpl;
import com.keita.restapi.item.Item;
import com.keita.restapi.item.ItemRepository;
import com.keita.restapi.item.ItemServiceImpl;
import com.keita.restapi.user.Role;
import com.keita.restapi.user.User;
import com.keita.restapi.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class InquiryServiceTest {

    @Mock
    private InquiryRepository inquiryRepositoryMock;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private AuthService authServiceMock;

    @InjectMocks
    private InquiryServiceImpl inquiryService;

    @InjectMocks
    private ItemServiceImpl itemService;

    // Verifies that all the inquiries are retrieved successfully
    @Test
    public void testGetAllInquiries() {
        //Initializing list of inquiries and get all of it
        List<Inquiry> expectedInquiries = new ArrayList<>();
        expectedInquiries.add(new Inquiry(1L, "Message1", null, null, null));
        expectedInquiries.add(new Inquiry(2L, "Message2", null, null, null));

        when(inquiryRepositoryMock.findAll()).thenReturn(expectedInquiries);

        List<Inquiry> actualInquiries = inquiryService.getAllInquiries();
        verify(inquiryRepositoryMock).findAll();

        //Assert expectedInquiries match the return list
        assertEquals(expectedInquiries, actualInquiries);
        //Assert expectedInquiries size is 2
        assertEquals(2, expectedInquiries.size());
    }
    // Verifies that all inquiries for a specific item id are retrieved successfully
    @Test
    public void getInquiriesByItemId() {
       // create a sample item object
       Long sampleItemId = 1L;
       Item sampleItem = new Item(sampleItemId, "Test item", null, "This is description", null, null);

       // create 2 sample users;
       User sampleUser1 = createUser(1L, "user1", "user1@example.com", "password", "USER");
       User sampleUser2 = createUser(2L, "user2", "user2@example.com", "password", "USER");

       // create list of inquiries
       List<Inquiry> expectedInquiries = new ArrayList<>();
       expectedInquiries.add(new Inquiry(1L, "Message1", null, sampleItem, sampleUser1));
       expectedInquiries.add(new Inquiry(1L, "Message1", null, sampleItem, sampleUser2));

       when(inquiryRepositoryMock.findAllByItemId(sampleItemId)).thenReturn(expectedInquiries);

       List<Inquiry> actualInquiries = inquiryService.getInquiriesByItemId(sampleItemId);

       int expectedInquiriesSize = 2;
       assertEquals(expectedInquiriesSize, actualInquiries.size());

       assertEquals(expectedInquiries.size(), actualInquiries.size());
       for (int i = 0; i < actualInquiries.size(); i++) {
           assertEquals(expectedInquiries.get(i).getMessage(), actualInquiries.get(i).getMessage());
           assertEquals(expectedInquiries.get(i).getMessage(), actualInquiries.get(i).getMessage());
           assertEquals(expectedInquiries.get(i).getItem().getId(), actualInquiries.get(i).getItem().getId());
           assertEquals(expectedInquiries.get(i).getItem().getName(), actualInquiries.get(i).getItem().getName());
           assertEquals(expectedInquiries.get(i).getUser().getId(), actualInquiries.get(i).getUser().getId());
           assertEquals(expectedInquiries.get(i).getUser().getUsername(), actualInquiries.get(i).getUser().getUsername());
       }
    }


    // Verifies that inquiries for a non-existing item id generates InquiryNotFoundException
    @Test
    public void getInquiriesByItemId_NonExistingItemId() {
        // This item id doesn't exist
        Long nonExistingItemId = 100L;

        when(inquiryRepositoryMock.findAllByItemId(nonExistingItemId)).thenReturn(Collections.emptyList());
        // assert inquries with the item id don't exist
        assertThrows(InquiryNotFoundException.class, () -> {
            inquiryService.getInquiriesByItemId(nonExistingItemId);
        });

        verify(inquiryRepositoryMock).findAllByItemId(nonExistingItemId);
    }

    // Verifies that an inquiry with an id is retrieved successfully
    @Test
    public void testGetInquiry_ExistingId() {
        //Initializing list of inquiries and get all of it
        List<Inquiry> expectedInquiries = new ArrayList<>();
        expectedInquiries.add(new Inquiry(1L, "Message1", null, null, null));
        expectedInquiries.add(new Inquiry(2L, "Message2", null, null, null));

        Inquiry expectedInquiry = expectedInquiries.get(0);
        when(inquiryRepositoryMock.findById(1L)).thenReturn(Optional.of(expectedInquiry));

        // Call the service
        Inquiry actualInquiry = inquiryService.getInquiry(1L);

        // Verify that repository was called by correct id
        verify(inquiryRepositoryMock).findById(1L);

        // Assert that expected inquiry matches actual inquiry
        assertEquals(expectedInquiry, actualInquiry);
    }

    // Verifies that an inquiry with a non-exisiting id generates InquiryNotFoundException
    @Test
    public void testGetInquiry_NonExistingId() {
        //Creating mock Inquiry objects
        List<Inquiry> expectedInquiries = new ArrayList<>();
        expectedInquiries.add(new Inquiry(1L, "Message1", null, null, null));
        expectedInquiries.add(new Inquiry(2L, "Message2", null, null, null));

        // call non-existing id inquiry object
        when(inquiryRepositoryMock.findById(3L)).thenReturn(Optional.empty());

        // Call the service and expect an custom exception
        assertThrows(InquiryNotFoundException.class, () -> {
            inquiryService.getInquiry(3L);
        });

        // Verify that the repository method was called with the correct ID
        verify(inquiryRepositoryMock).findById(3L);
    }

    // Verifies that an inquiry for an item from user is posted successfully
    @Test
    public void testSaveInquiry_successful() {
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

        // Creating the inquiry to be saved
        Inquiry inquiry = new Inquiry();
        inquiry.setMessage("New inquiry message");

        // Saving the inquiry
        inquiryService.saveInquiry(inquiry, itemId);

        // Verifying repository method calls
        verify(itemRepositoryMock).findById(itemId);
        verify(userRepositoryMock).findByUsername("testUser");
        verify(inquiryRepositoryMock).save(inquiry);
    }

    // Verifies that an inquiry for an item from user TWICE generates RUNTIME Exception.
    @Test
    public void testSaveInquiry_sameInquiryTwice() {
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
        // Mocking inquiryRepository to return an existing inquiry for user1 and itemA
        when(inquiryRepositoryMock.findByItemIdAndUserId(itemId, user1.getId())).thenReturn(Optional.of(new Inquiry()));

        // Creating the inquiry to be saved
        Inquiry inquiry = new Inquiry();
        inquiry.setMessage("New inquiry message");

        // Saving the inquiry and expecting an exception
        assertThrows(RuntimeException.class, () -> inquiryService.saveInquiry(inquiry, itemId));

        // Verifying repository method calls
        verify(itemRepositoryMock).findById(itemId);
        verify(userRepositoryMock).findByUsername(user1.getUsername());
        verify(inquiryRepositoryMock).findByItemIdAndUserId(itemId, user1.getId());
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


