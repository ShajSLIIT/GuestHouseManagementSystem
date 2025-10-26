package com.ghm.guesthousemanagementsystem.exeption;

public class TokenInactiveException extends RuntimeException {
    public TokenInactiveException(String message) {
        super(message);
    }
}
