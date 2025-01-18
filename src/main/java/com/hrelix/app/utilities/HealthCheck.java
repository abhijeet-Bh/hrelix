package com.hrelix.app.utilities;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("api/v1/healthz")
    @Tag(name = "Open Endpoints", description = "Operations related to healthcheck and login")
    public ResponseEntity<ApiResponse> healthCheck() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "Request Successful!",
                        "HRelix is working fine!"
                ));
    }
}
