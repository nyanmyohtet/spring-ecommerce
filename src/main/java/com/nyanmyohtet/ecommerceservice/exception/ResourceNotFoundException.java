package com.nyanmyohtet.ecommerceservice.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1238462578349834273L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
