package com.hrelix.app.controllers;


import com.hrelix.app.dtos.EmployeeDTO;
import com.hrelix.app.models.Employee;
import com.hrelix.app.models.mappers.EmployeeMapper;
import com.hrelix.app.services.EmployeeService;
import com.hrelix.app.utils.ApiResponse;
import com.hrelix.app.utils.ErrorResponse;
import com.hrelix.app.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // POST: Create a new employee
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
            return new ResponseEntity<>(new SuccessResponse<EmployeeDTO>(true, 201, createdEmployee), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(403, e.getMessage()), HttpStatus.BAD_GATEWAY);
        }
    }

    // GET: Retrieve a specific employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable String id) {
        try {
            Employee employee = employeeService.findById(UUID.fromString(id));
            return new ResponseEntity<>(new SuccessResponse<>(true, 200, EmployeeMapper.toDTO(employee)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // GET: Retrieve all employees
    @GetMapping
    public ResponseEntity<ApiResponse> getAllEmployees() {
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployees();
            return new ResponseEntity<>(new SuccessResponse<>(true, 200, employees), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(403, "Something went wrong!"), HttpStatus.BAD_REQUEST);
        }
    }
}
