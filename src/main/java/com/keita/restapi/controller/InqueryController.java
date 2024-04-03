package com.keita.restapi.controller;

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

import com.keita.restapi.model.Inquery;
import com.keita.restapi.service.InqueryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inquery")
public class InqueryController {

    @Autowired
    InqueryService inqueryService;

    @GetMapping("/all")
    public ResponseEntity<List<Inquery>> getAllInqueries() {
        // get all inqueries
        return new ResponseEntity<>(inqueryService.getAllInqueries(), HttpStatus.OK);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Inquery>> getInqueriesByItemId(@PathVariable Long itemId) {
        // get all inqueries by item id
        return new ResponseEntity<>(inqueryService.getInqueriesByItemId(itemId), HttpStatus.OK);
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<Inquery> createInquery(@Valid @RequestBody Inquery inquery, @PathVariable Long itemId) {
        // create a new Inquery with item id
        return new ResponseEntity<>(inqueryService.saveInquery(inquery, itemId), HttpStatus.CREATED);
    }
}
