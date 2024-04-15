package com.keita.restapi.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.keita.restapi.item.Item;
import com.keita.restapi.item.ItemNotFoundException;
import com.keita.restapi.item.ItemRepository;
import com.keita.restapi.user.User;
import com.keita.restapi.user.UserRepository;

/**
 * Service implementation for managing inquiries.
 */
@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all inquiries from the database.
     *
     * @return A list of all inquiries.
     */
    @Override
    public List<Inquiry> getAllInquiries() {
        // get all Inquiry objeccts
        return (List<Inquiry>) inquiryRepository.findAll();
    }

    /**
     * Retrieves all inquiries associated with a specific item ID.
     *
     * @param itemId The ID of the item to retrieve inquiries for.
     * @return A list of inquiries associated with the specified item ID.
     * @throws InquiryNotFoundException If no inquiries are found for the given item ID.
     */
    @Override
    public List<Inquiry> getInquiriesByItemId(Long itemId) {
        List<Inquiry> inquiries = inquiryRepository.findAllByItemId(itemId);

        if(inquiries.isEmpty()) {
            throw new InquiryNotFoundException("No inquiries found for item id: " + itemId);
        }

        return inquiries;
    }

    /**
     * Retrieves an inquiry by its ID.
     *
     * @param inquiryId The ID of the inquiry to retrieve.
     * @return The inquiry with the specified ID.
     * @throws InquiryNotFoundException If no inquiry is found with the given ID.
     */
    @Override
    public Inquiry getInquiry(Long inquiryId) {
        // get inquiry object by id. Create exception if it doesn't exist
        return inquiryRepository.findById(inquiryId)
                    .orElseThrow(() -> new InquiryNotFoundException("Inquiry with ID " + inquiryId + " isn't found"));
    }

    /**
     * Saves a new inquiry to the database, associating it with the specified item ID and user.
     *
     * @param inquiry  The inquiry to save.
     * @param itemId   The ID of the item associated with the inquiry.
     * @return The saved inquiry.
     * @throws ItemNotFoundException    If no item is found with the given item ID.
     * @throws RuntimeException        If the user with the given username is not found.
     * @throws RuntimeException       If the user sends an inquiry for the same item.
     */
    @Override
    public Inquiry saveInquiry(Inquiry inquiry, Long itemId) {
        //Retrieve the principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Item targetItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id: " + itemId + " isn't found"));


        //  Retrieve user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User : " + username + " not found"));

        inquiryRepository.findByItemIdAndUserId(itemId, user.getId())
                .ifPresent(existingInquiry -> {
                    throw new RuntimeException("User : " + user.getId()
                    + " has already send an inquiry for the item : " + itemId);
                });

        inquiry.setUser(user);
        inquiry.setItem(targetItem);
        return inquiryRepository.save(inquiry);
    }



}
