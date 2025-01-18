package com.hrelix.app.employee;


import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.ErrorResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        SuccessResponse<EmployeeDTO> response = new SuccessResponse<>(
                "User created successfully",
                createdEmployee
        );
        return ResponseEntity.ok(response);

    }

    // GET: Retrieve a specific employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.findById(UUID.fromString(id));
        SuccessResponse<EmployeeDTO> response = new SuccessResponse<>(
                "User retrieved successfully",
                EmployeeDTO.builder()
                        .id(employee.getId())
                        .firstName(employee.getFirstName())
                        .lastName(employee.getLastName())
                        .email(employee.getEmail())
                        .phone(employee.getPhone())
                        .salary(employee.getSalary())
                        .joiningDate(employee.getJoiningDate())
                        .build()
        );
        return ResponseEntity.ok(response);
    }

    // GET: Retrieve all employees
    @GetMapping
    public ResponseEntity<ApiResponse> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        SuccessResponse<List<EmployeeDTO>> response = new SuccessResponse<>(
                "Users retrieved successfully",
                employees
        );
        return ResponseEntity.ok(response);

    }

    // DELETE: Delete employee
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable UUID id) {
        Employee employee = employeeService.findById(id);
        if (employee != null && employeeService.deleteEmployee(employee)) {
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "Employee Deleted Successfully!",
                            employee
                    ));
        } else
            return ResponseEntity.status(404).body(
                    new ErrorResponse(
                            "Employee Not Found!",
                            null
                    ));
    }

    // PUT : update Employee Details
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable UUID id, @RequestBody EmployeeDTO employee) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employee, id);
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "Employee Updated Successfully!",
                        updatedEmployee
                ));
    }

    // GET: Profile od current user
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile() {
        EmployeeDTO profile = employeeService.getProfile();
        return ResponseEntity.ok(new SuccessResponse<>("Retrieved user successfully!", profile));
    }

}
