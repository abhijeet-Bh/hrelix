package com.hrelix.app.payroll;

import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payroll")
@Tag(name = "Payroll Endpoints", description = "Operations related to Payroll of the Employees")
public class PayrollController {

    @Autowired
    private EmployeeCtcService employeeCtcService;

    @Autowired
    private DeductionService deductionService;

    @PostMapping("/generate-ctc")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> createEmployeeCtc(@RequestBody EmployeeCTC employeeCTC) {
        EmployeeCTC createdEmployeeCtc = employeeCtcService.createCtc(employeeCTC);
        SuccessResponse<EmployeeCTC> response = new SuccessResponse<>(
                "CTC created successfully",
                createdEmployeeCtc
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-deductions")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> createEmployeeDeduction(@RequestBody Deductions deduction) {
        Deductions createdEmployeeDeduction = deductionService.createDeduction(deduction);
        SuccessResponse<Deductions> response = new SuccessResponse<>(
                "Employee Deduction created successfully",
                createdEmployeeDeduction
        );
        return ResponseEntity.ok(response);
    }

}
