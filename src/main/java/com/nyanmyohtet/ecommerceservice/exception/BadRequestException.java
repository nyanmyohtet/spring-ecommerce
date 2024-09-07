package com.nyanmyohtet.ecommerceservice.exception;

import java.io.Serial;

public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 982472985983223471L;

    public BadRequestException(String message) {
        super(message);
    }
}
