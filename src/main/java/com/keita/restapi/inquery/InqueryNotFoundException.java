package com.keita.restapi.inquery;

public class InqueryNotFoundException extends RuntimeException {

    public InqueryNotFoundException(String message) {
        super(message);
    }
}
