package com.keita.restapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.keita.restapi.model.Inquery;

public interface InqueryRepository extends CrudRepository<Inquery, Long> {
    Optional<Inquery> findById(Long inqueryId);
    Optional<Inquery> findByItemId(Long itemId);
}
