package com.doctolib.childweight.exception;

public class ChildNotFoundException extends RuntimeException {

    public ChildNotFoundException(Long childId) {
        super("Child with ID " + childId + " does not exist");
    }
}