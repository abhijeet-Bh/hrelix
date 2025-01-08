package com.hrelix.app.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hrelix.app.utils.ApiResponse;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse extends ApiResponse {

    @JsonProperty("data")
    private String message;

    public ErrorResponse(int statusCode, String message) {
        super(false, statusCode); // Call to the parent constructor
        this.message = message;
    }

}
