package com.naveen.guesthousemanagementsystem.exeption;

public class RoomUnavailableException extends RuntimeException {
    public RoomUnavailableException(String message) {
        super(message);
    }
}
