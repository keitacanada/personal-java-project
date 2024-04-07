package com.keita.restapi.item;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long>{
    Optional<Item> findById(Long itemId);
    Optional<Item> findByName(String name);
    void deleteById(Long itemid);
}
