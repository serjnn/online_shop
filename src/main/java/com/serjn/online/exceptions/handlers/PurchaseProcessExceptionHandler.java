package com.serjn.online.exceptions.handlers;


import com.serjn.online.exceptions.EmptyAddressException;
import com.serjn.online.exceptions.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PurchaseProcessExceptionHandler {

    @ExceptionHandler(EmptyAddressException.class)
    public ResponseEntity<String> handleEmptyAddress() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Client's address is not stated");
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Client does not have enough money");
    }
}
