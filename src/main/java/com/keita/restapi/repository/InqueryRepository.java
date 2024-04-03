package com.keita.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.keita.restapi.model.Inquery;

public interface InqueryRepository extends CrudRepository<Inquery, Long> {
    Optional<Inquery> findById(Long inqueryId);
    List<Inquery> findAllByItemId(Long itemId);
}
