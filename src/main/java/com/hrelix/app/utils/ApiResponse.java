package com.hrelix.app.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ApiResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("status-code")
    private int statusCode;

    protected ApiResponse(boolean success, int statusCode) {
        this.success = success;
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
