package com.keita.restapi.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keita.restapi.exception.InqueryNotFoundException;
import com.keita.restapi.model.Inquery;
import com.keita.restapi.repository.InqueryRepository;
import com.keita.restapi.repository.ItemRepository;

@Service
public class InqueryServiceImpl implements InqueryService {

    @Autowired
    private InqueryRepository inqueryRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Inquery> getAllInqueries() {
        return (List<Inquery>) inqueryRepository.findAll();
    }

    @Override
    public Inquery getInquery(Long inqueryId) {
        Optional<Inquery> potentialInquery = inqueryRepository.findById(inqueryId);

        if(!potentialInquery.isPresent()) {
            throw new InqueryNotFoundException("Inquery with ID " + inqueryId + " isn't found");
        }

        return potentialInquery.get();
    }

    @Override
    public Inquery getInqueryByItemId(Long itemId) {
        //Get Inquiry by item id
        Optional<Inquery> potentialInquery = inqueryRepository.findByItemId(itemId);
        if (!potentialInquery.isPresent()) {
            throw new InqueryNotFoundException("Inquery for item ID " + itemId + " isn't found");
        }
        return potentialInquery.get();
    }

}
