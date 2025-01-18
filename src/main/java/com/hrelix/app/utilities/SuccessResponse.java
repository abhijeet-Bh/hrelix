package com.hrelix.app.utilities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuccessResponse<T> extends ApiResponse {

    public SuccessResponse(String message, T data) {
        super(true, message, data, null);
    }
}
