package com.keita.restapi.inquery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keita.restapi.user.UserRepository;

import jakarta.validation.Valid;

/**
 * Controller class for handling inquiries related endpoints.
 */
@RestController
@RequestMapping("/inquery")
public class InqueryController {

    @Autowired
    InqueryService inqueryService;

    @Autowired
    UserRepository userRepository;

    /**
     * Endpoint to retrieve all inquiries from ADMIN user.
     *
     * @return ResponseEntity containing a list of all inquiries and HTTP status OK if successful.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Inquery>> getAllInqueries() {
        // get all inqueries
        return new ResponseEntity<>(inqueryService.getAllInqueries(), HttpStatus.OK);
    }
    /**
     * Endpoint to retrieve inquiries by item ID from all login users.
     *
     * @param itemId ID of the item to retrieve inquiries for.
     * @return ResponseEntity containing a list of inquiries for the specified item and HTTP status OK if successful.
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Inquery>> getInqueriesByItemId(@PathVariable Long itemId) {
        // get all inqueries by item id
        return new ResponseEntity<>(inqueryService.getInqueriesByItemId(itemId), HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve inquiries by item ID.
     *
     * @param itemId ID of the item to retrieve inquiries for.
     * @return ResponseEntity containing a list of inquiries for the specified item and HTTP status OK if successful.
     */
    @PostMapping("/item/{itemId}")
    public ResponseEntity<Inquery> createInquery(@Valid @RequestBody Inquery inquery, @PathVariable Long itemId) {
        return new ResponseEntity<>(inqueryService.saveInquery(inquery, itemId), HttpStatus.CREATED);
    }
}
