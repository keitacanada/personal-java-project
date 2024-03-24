package com.keita.restapi.service;

import java.util.List;
import java.util.Optional;

import com.keita.restapi.model.Inquery;

public interface InqueryService {
    //Inquery getInqueryById(Long inqueryId);
    List<Inquery> getAllInqueries();
    Inquery getInquery(Long inqueryId);
}
