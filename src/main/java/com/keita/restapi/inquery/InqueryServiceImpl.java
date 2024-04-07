package com.keita.restapi.inquery;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keita.restapi.item.Item;
import com.keita.restapi.item.ItemNotFoundException;
import com.keita.restapi.item.ItemRepository;

@Service
public class InqueryServiceImpl implements InqueryService {

    @Autowired
    private InqueryRepository inqueryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Inquery> getAllInqueries() {
        // get all Inquery objeccts
        return (List<Inquery>) inqueryRepository.findAll();
    }

    @Override
    public Inquery getInquery(Long inqueryId) {
        // get inquery object by id. Create exception if it doesn't exist
        Optional<Inquery> potentialInquery = inqueryRepository.findById(inqueryId);

        if(!potentialInquery.isPresent()) {
            throw new InqueryNotFoundException("Inquery with ID " + inqueryId + " isn't found");
        }

        return potentialInquery.get();
    }

    @Override
    public List<Inquery> getInqueriesByItemId(Long itemId) {
        // Get inquery object by item id. Create exception if it doesn't exist
        List<Inquery> inqueries = inqueryRepository.findAllByItemId(itemId);

        if(inqueries.isEmpty()) {
            throw new InqueryNotFoundException("No inqueries found for item id: " + itemId);
        }

        return inqueries;
    }

    @Override
    public Inquery saveInquery(Inquery inquery, Long itemId) {
        Optional<Item> targetItem = itemRepository.findById(itemId);
        if(!targetItem.isPresent()) {
            throw new ItemNotFoundException("Item with id: " + itemId + " isn't found");
        }

        inquery.setItem(targetItem.get());
        return inqueryRepository.save(inquery);
    }

}
