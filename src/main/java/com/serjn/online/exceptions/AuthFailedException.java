package com.serjn.online.exceptions;

public class AuthFailedException extends RuntimeException {
    public AuthFailedException(String message) {
        super(message);
    }

}
