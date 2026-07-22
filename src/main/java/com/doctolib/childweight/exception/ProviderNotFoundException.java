package com.doctolib.childweight.exception;

public class ProviderNotFoundException extends RuntimeException {

    public ProviderNotFoundException(Long providerId) {
        super("provider with ID " + providerId + " does not exist");
    }
}
