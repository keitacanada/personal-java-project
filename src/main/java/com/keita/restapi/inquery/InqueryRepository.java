package com.keita.restapi.inquery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface InqueryRepository extends CrudRepository<Inquery, Long> {
    Optional<Inquery> findById(Long inqueryId);
    List<Inquery> findAllByItemId(Long itemId);
    Optional<Inquery> findByItemIdAndUserId(Long itemId, Long userId);
}
