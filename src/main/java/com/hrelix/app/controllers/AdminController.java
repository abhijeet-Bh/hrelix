package com.hrelix.app.controllers;

import com.hrelix.app.dtos.EmployeeDTO;
import com.hrelix.app.models.Employee;
import com.hrelix.app.models.utils.Role;
import com.hrelix.app.repositories.EmployeeRepository;
import com.hrelix.app.utils.ApiResponse;
import com.hrelix.app.utils.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private EmployeeRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerAdmin(@RequestBody EmployeeDTO adminDTO) {
        // Check if any users exist in the database
        if (userRepository.count() > 0) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Admin user already exists."), HttpStatus.BAD_REQUEST);
        }

        // Create the admin user
        Employee admin = new Employee();
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword())); // Password is encrypted
        admin.setRoles(Collections.singletonList(Role.ADMIN));  // Assign the admin role
        userRepository.save(admin);

        return new ResponseEntity<>(new ErrorResponse(201, "Admin user already exists."), HttpStatus.BAD_REQUEST);
    }
}
