package com.keita.restapi.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/inquiry")
public class InquiryController {

    @Autowired
    InquiryService inquiryService;

    @Autowired
    UserRepository userRepository;

    /**
     * Endpoint to retrieve all inquiries from ADMIN user.
     *
     * @return ResponseEntity containing a list of all inquiries and HTTP status OK if successful.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Inquiry>> getAllInquiries() {
        // get all inquiries
        return new ResponseEntity<>(inquiryService.getAllInquiries(), HttpStatus.OK);
    }
    /**
     * Endpoint to retrieve inquiries by item ID from all login users.
     *
     * @param itemId ID of the item to retrieve inquiries for.
     * @return ResponseEntity containing a list of inquiries for the specified item and HTTP status OK if successful.
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Inquiry>> getInquiriesByItemId(@PathVariable Long itemId) {
        // get all inquiries by item id
        return new ResponseEntity<>(inquiryService.getInquiriesByItemId(itemId), HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve inquiries by item ID.
     *
     * @param itemId ID of the item to retrieve inquiries for.
     * @return ResponseEntity containing a list of inquiries for the specified item and HTTP status OK if successful.
     */
    @PostMapping("/item/{itemId}")
    public ResponseEntity<Inquiry> createInquiry(@Valid @RequestBody Inquiry inquiry, @PathVariable Long itemId) {
        return new ResponseEntity<>(inquiryService.saveInquiry(inquiry, itemId), HttpStatus.CREATED);
    }
}
