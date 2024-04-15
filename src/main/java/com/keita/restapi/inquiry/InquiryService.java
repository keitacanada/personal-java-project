package com.keita.restapi.inquiry;

import java.util.List;

public interface InquiryService {
    //Inquiry getInquiryById(Long inquiryId);
    List<Inquiry> getAllInquiries();
    Inquiry getInquiry(Long inquiryId);
    List<Inquiry> getInquiriesByItemId(Long itemId);
    Inquiry saveInquiry(Inquiry inquiry, Long itemId);

}
