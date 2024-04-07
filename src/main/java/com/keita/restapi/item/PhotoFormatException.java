package com.keita.restapi.item;

public class PhotoFormatException extends RuntimeException{
    public PhotoFormatException(String message) {
        super(message);
    }

    public PhotoFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
