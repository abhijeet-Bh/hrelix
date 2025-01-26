package com.hrelix.app.exceptions;

public class PayrollNotFound extends RuntimeException {
    public PayrollNotFound(String message) {
        super(message);
    }
}
