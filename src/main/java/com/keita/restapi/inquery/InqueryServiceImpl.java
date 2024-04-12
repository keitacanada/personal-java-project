package com.keita.restapi.inquery;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
public class InqueryServiceImpl implements InqueryService {

    @Autowired
    private InqueryRepository inqueryRepository;

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
    public List<Inquery> getAllInqueries() {
        // get all Inquery objeccts
        return (List<Inquery>) inqueryRepository.findAll();
    }

    /**
     * Retrieves an inquiry by its ID.
     *
     * @param inqueryId The ID of the inquiry to retrieve.
     * @return The inquiry with the specified ID.
     * @throws InqueryNotFoundException If no inquiry is found with the given ID.
     */
    @Override
    public Inquery getInquery(Long inqueryId) {
        // get inquery object by id. Create exception if it doesn't exist
        Optional<Inquery> potentialInquery = inqueryRepository.findById(inqueryId);

        if(!potentialInquery.isPresent()) {
            throw new InqueryNotFoundException("Inquery with ID " + inqueryId + " isn't found");
        }

        return potentialInquery.get();
    }

    /**
     * Retrieves all inquiries associated with a specific item ID.
     *
     * @param itemId The ID of the item to retrieve inquiries for.
     * @return A list of inquiries associated with the specified item ID.
     * @throws InqueryNotFoundException If no inquiries are found for the given item ID.
     */
    @Override
    public List<Inquery> getInqueriesByItemId(Long itemId) {
        // Get inquery object by item id. Create exception if it doesn't exist
        List<Inquery> inqueries = inqueryRepository.findAllByItemId(itemId);

        if(inqueries.isEmpty()) {
            throw new InqueryNotFoundException("No inqueries found for item id: " + itemId);
        }

        return inqueries;
    }

    /**
     * Saves a new inquiry to the database, associating it with the specified item ID and user.
     *
     * @param inquery  The inquiry to save.
     * @param itemId   The ID of the item associated with the inquiry.
     * @param username The username of the user associated with the inquiry.
     * @return The saved inquiry.
     * @throws ItemNotFoundException    If no item is found with the given item ID.
     * @throws RuntimeException        If the user with the given username is not found.
     */
    @Override
    public Inquery saveInquery(Inquery inquery, Long itemId, String username) {
        Optional<Item> targetItem = itemRepository.findById(itemId);
        if(!targetItem.isPresent()) {
            throw new ItemNotFoundException("Item with id: " + itemId + " isn't found");
        }

        //  Retrieve user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User : " + username + " not found"));

        inquery.setUser(user);
        inquery.setItem(targetItem.get());
        return inqueryRepository.save(inquery);
    }
}
