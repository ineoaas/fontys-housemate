package org.example.housematesolution.Presentation.controllers;

import org.example.housematesolution.Business.exceptions.EmailAlreadyUsedException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Turns business exceptions into HTTP responses for every controller
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HouseNotFoundException.class)
    public ResponseEntity<String> handleHouseNotFound(HouseNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<String> handleEmailUsed(EmailAlreadyUsedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
