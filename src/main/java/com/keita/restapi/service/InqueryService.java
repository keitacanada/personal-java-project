package com.keita.restapi.service;

import java.util.List;
import com.keita.restapi.model.Inquery;

public interface InqueryService {
    //Inquery getInqueryById(Long inqueryId);
    List<Inquery> getAllInqueries();
}
