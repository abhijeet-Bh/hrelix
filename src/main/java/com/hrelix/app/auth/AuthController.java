package com.hrelix.app.auth;

import com.hrelix.app.configs.JwtUtils;
import com.hrelix.app.employee.Employee;
import com.hrelix.app.employee.EmployeeService;
import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.ErrorResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
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
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            final Employee userDetails = employeeService.findByEmail(authRequest.getEmail());
            String accessToken = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(new SuccessResponse<String>(true, 200, accessToken), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(403, e.getMessage()), HttpStatus.BAD_GATEWAY);
        }
    }
}