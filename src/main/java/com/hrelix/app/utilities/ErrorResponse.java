package com.hrelix.app.utilities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse extends ApiResponse {

    public ErrorResponse(String message, Object errorDetails) {
        super(false, message, null, errorDetails);
    }
}
