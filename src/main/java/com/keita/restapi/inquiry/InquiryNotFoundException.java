package com.keita.restapi.inquiry;

public class InquiryNotFoundException extends RuntimeException {

    public InquiryNotFoundException(String message) {
        super(message);
    }
}
