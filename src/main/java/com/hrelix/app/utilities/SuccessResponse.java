package com.hrelix.app.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuccessResponse<T> extends ApiResponse {

    @JsonProperty("data")
    private T data;

    public SuccessResponse(boolean success, int statusCode, T data) {
        super(success, statusCode); // Call to the parent constructor
        this.data = data;
    }

}
