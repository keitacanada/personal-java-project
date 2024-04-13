package com.keita.restapi.inquery;

import java.util.List;

public interface InqueryService {
    //Inquery getInqueryById(Long inqueryId);
    List<Inquery> getAllInqueries();
    Inquery getInquery(Long inqueryId);
    List<Inquery> getInqueriesByItemId(Long itemId);
    Inquery saveInquery(Inquery inquery, Long itemId, String username);

}
