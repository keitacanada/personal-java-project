package com.keita.restapi.exception;

public class InqueryNotFoundException extends RuntimeException {

    public InqueryNotFoundException(String message) {
        super(message);
    }
}
