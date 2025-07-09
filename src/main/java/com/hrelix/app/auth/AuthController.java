package com.hrelix.app.auth;

import com.hrelix.app.configs.JwtUtils;
import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeService;
import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@Tag(name = "Open Endpoints", description = "Operations related to healthcheck and login")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtUtils jwtUtil;

    @Operation(
            description = "Login Endpoint",
            summary = "You can login using test credentials to get accessToken from here!"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest authRequest) throws Exception {
        log.info("Login Called ====> :)");
        final Employee userDetails = employeeService.findByEmail(authRequest.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        String accessToken = jwtUtil.generateToken(userDetails);
        Map<String, String> authResponse = new HashMap<>();
        authResponse.put("accessToken", accessToken);
        authResponse.put("refreshToken", null);
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "LoggedIn Successfully!",
                        authResponse
                ));

    }
}