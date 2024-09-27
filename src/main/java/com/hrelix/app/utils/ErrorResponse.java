package com.hrelix.app.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse extends ApiResponse {

    @JsonProperty("data")
    private String message;

    public ErrorResponse(boolean success, int statusCode, String message) {
        super(success, statusCode); // Call to the parent constructor
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
