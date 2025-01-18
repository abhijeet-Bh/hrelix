package com.hrelix.app.exceptions;

public class LeaveNotFoundException extends RuntimeException {
    public LeaveNotFoundException(String message) {
        super(message);
    }
}
