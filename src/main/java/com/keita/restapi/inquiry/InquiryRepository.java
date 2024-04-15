package com.keita.restapi.inquiry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface InquiryRepository extends CrudRepository<Inquiry, Long> {
    Optional<Inquiry> findById(Long inquiryId);
    List<Inquiry> findAllByItemId(Long itemId);
    Optional<Inquiry> findByItemIdAndUserId(Long itemId, Long userId);
}
