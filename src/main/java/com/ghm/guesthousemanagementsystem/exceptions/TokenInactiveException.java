package com.ghm.guesthousemanagementsystem.exceptions;

public class TokenInactiveException extends RuntimeException {
    public TokenInactiveException(String message) {
        super(message);
    }
}
