package com.serjn.online.exceptions.handlers;

import com.serjn.online.exceptions.AuthFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionsHandler {

    @ExceptionHandler(AuthFailedException.class)
    public ResponseEntity<String> handleNoSuchProductException(AuthFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}
