package com.naveen.guesthousemanagementsystem.exeption;

public class TokenInactiveException extends RuntimeException {
    public TokenInactiveException(String message) {
        super(message);
    }
}
