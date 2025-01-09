package com.hrelix.app.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class ApiResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("status-code")
    private int statusCode;

    protected ApiResponse(boolean success, int statusCode) {
        this.success = success;
        this.statusCode = statusCode;
    }

}
