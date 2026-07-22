package com.doctolib.childweight.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
	@ExceptionHandler(ChildNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleChildNotFound(ChildNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage())); // Output: {"error": "Child with ID 123 does not exist"}
    }
	
	@ExceptionHandler(ProviderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProviderNotFound(ProviderNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage())); // Output: {"error": "Provider with ID ABC123 does not exist"}
    }
}