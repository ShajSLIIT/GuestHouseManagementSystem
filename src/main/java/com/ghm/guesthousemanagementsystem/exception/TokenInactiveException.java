package com.ghm.guesthousemanagementsystem.exception;

public class TokenInactiveException extends RuntimeException {
    public TokenInactiveException(String message) {
        super(message);
    }
}
