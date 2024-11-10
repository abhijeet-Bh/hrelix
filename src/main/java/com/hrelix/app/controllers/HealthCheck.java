package com.hrelix.app.controllers;

import com.hrelix.app.utils.ApiResponse;
import com.hrelix.app.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("api/v1/healthz")
    public ResponseEntity<ApiResponse> healthCheck() {
        return ResponseEntity.ok(new SuccessResponse<>(
                true,
                200,
                "HRelix is working fine!"
        ));
    }
}
