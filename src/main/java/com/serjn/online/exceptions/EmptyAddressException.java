package com.serjn.online.exceptions;

public class EmptyAddressException extends RuntimeException {
    public EmptyAddressException(String message) {
        super(message);
    }
}
