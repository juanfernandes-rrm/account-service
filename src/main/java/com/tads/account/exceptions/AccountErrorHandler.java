package com.tads.account.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;

@RestControllerAdvice
public class AccountErrorHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity handleMethodArgumentNotValidException(AccountNotFoundException ex) {
        var errors = ex.getMessage();
        return ResponseEntity.badRequest().body(errors);
    }
}
