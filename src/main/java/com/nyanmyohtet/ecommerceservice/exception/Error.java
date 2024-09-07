package com.nyanmyohtet.ecommerceservice.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Error {
    private String message;
    private int status;
    private Long timestamp;
}

