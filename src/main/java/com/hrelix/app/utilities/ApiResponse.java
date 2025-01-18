package com.hrelix.app.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class ApiResponse {

    @JsonProperty("success")
    private final boolean success;

    @JsonProperty("message")
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    private final Object data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error")
    private final Object error;

    protected ApiResponse(boolean success, String message, Object data, Object error) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
    }
}
