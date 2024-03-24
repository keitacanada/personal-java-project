package com.keita.restapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.keita.restapi.model.Item;

public interface ItemRepository extends CrudRepository<Item, Long>{
    Optional<Item> findById(Long itemId);
}
