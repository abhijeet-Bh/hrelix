package com.hrelix.app.employee;


import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.ErrorResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Endpoints", description = "Operations related to Employee for HR and Employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // POST: Create a new employee
    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
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

    // DELETE: Delete employee
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable UUID id) {
        Employee employee = employeeService.findById(id);
        if (employee != null && employeeService.deleteEmployee(employee)) {
            return new ResponseEntity<>(new SuccessResponse<>(true, 200, "Employee Deleted"), HttpStatus.OK);
        } else
            return new ResponseEntity<>(new ErrorResponse(404, "Employee Not Found!"), HttpStatus.NOT_FOUND);
    }

    // PUT : update Employee Details
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable UUID id, @RequestBody EmployeeDTO employee) {
        try {
            EmployeeDTO updatedEmployee = employeeService.updateEmployee(employee, id);
            return new ResponseEntity<>(new SuccessResponse<>(true, 200, updatedEmployee), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErrorResponse(503, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET: Profile od current user
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile() {
        EmployeeDTO profile = employeeService.getProfile();
        return new ResponseEntity<>(new SuccessResponse<>(true, 200, profile), HttpStatus.OK);
    }

}
