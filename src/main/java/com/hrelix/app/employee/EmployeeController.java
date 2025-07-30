package com.hrelix.app.employee;


import com.hrelix.app.exceptions.BankAccountNotFoundException;
import com.hrelix.app.exceptions.DeductionNotFound;
import com.hrelix.app.exceptions.EmployeeCtcNotFound;
import com.hrelix.app.payroll.*;
import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.ErrorResponse;
import com.hrelix.app.utilities.S3Service;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Endpoints", description = "Operations related to Employee for HR and Employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final S3Service s3Service;
    private final EmployeeCtcService employeeCtcService;
    private final DeductionService deductionService;
    private final BankAccountDetailService bankAccountDetailService;

    public EmployeeController(EmployeeService employeeService, S3Service s3Service, EmployeeCtcService employeeCtcService, DeductionService deductionService, BankAccountDetailService bankAccountDetailService) {
        this.employeeService = employeeService;
        this.s3Service = s3Service;
        this.employeeCtcService = employeeCtcService;
        this.deductionService = deductionService;
        this.bankAccountDetailService = bankAccountDetailService;
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

    // POST: Upload employee Avatar
    @PostMapping("/{id}/upload-profile")
    public ResponseEntity<ApiResponse> uploadProfile(
            @PathVariable UUID id,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        Employee employee = employeeService.findById(id);

        String folder = "profiles";
        String s3Url = s3Service.uploadFile(image, folder);

        employee.setAvatar(s3Url);
        EmployeeDTO updatedEmployee = employeeService.updateEmployeeAvatar(employee);

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "profile picture updated successfully!",
                        updatedEmployee
                ));
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
                        .avatar(employee.getAvatar())
                        .team(employee.getTeam())
                        .position(employee.getPosition())
                        .roles(employee.getRoles())
                        .build()
        );
        return ResponseEntity.ok(response);
    }

    // GET: get employee by email
    @GetMapping("by-email/{email}")
    public ResponseEntity<ApiResponse> getEmployeeByEmail(@PathVariable String email) throws Exception {
        Employee employee = employeeService.findByEmail(email);
        EmployeeCTC employeeCTC = null;
        Deductions deductions = null;
        BankAccountDetail bankAccountDetail = null;

        try {
            employeeCTC = employeeCtcService.findByEmployeeId(employee.getId());
        } catch (EmployeeCtcNotFound ignored) {
        }

        try {
            deductions = deductionService.findByEmployeeId(employee.getId());
        } catch (DeductionNotFound ignored) {
        }

        try {
            bankAccountDetail = bankAccountDetailService.findByEmployeeId(employee.getId());
        } catch (BankAccountNotFoundException ignored) {
        }

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "Employee details retrieved successfully!",
                        EmployeeDetailsDTO.builder()
                                .employee(EmployeeMapper.toDTO(employee))
                                .employeeCTC(employeeCTC)
                                .deductions(deductions)
                                .bankAccountDetail(bankAccountDetail)
                                .build()
                ));
    }

    //GET: search employee by email or name
    @GetMapping("search/{key}")
    public ResponseEntity<ApiResponse> searchEmployee(@PathVariable String key) {
        List<EmployeeDTO> results = employeeService.searchEmployeeByEmailOrName(key);

        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "Employees retrieved successfully!",
                        results
                ));
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
