package com.hrelix.app.payroll;

import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payroll")
@Tag(name = "Payroll Endpoints", description = "Operations related to Payroll of the Employees")
public class PayrollController {

    @Autowired
    private EmployeeCtcService employeeCtcService;

    @Autowired
    private DeductionService deductionService;

    @Autowired
    private PayrollService payrollService;

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

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> createNewPayroll(@RequestBody Payroll payroll) {
        Payroll createdPayroll = payrollService.createDeduction(payroll);
        SuccessResponse<Payroll> response = new SuccessResponse<>(
                "New Payroll created successfully",
                createdPayroll
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPayroll() {
        List<Payroll> allPayrolls = payrollService.getAllPayrolls();
        SuccessResponse<List<Payroll>> response = new SuccessResponse<>(
                "All Payrolls Retrieved successfully",
                allPayrolls
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getPayrollsByStatus(@RequestParam PaymentStatus query) {
        List<Payroll> allPayrolls = payrollService.getPayrollsByStatus(query);
        SuccessResponse<List<Payroll>> response = new SuccessResponse<>(
                "Payrolls by status Retrieved successfully",
                allPayrolls
        );
        return ResponseEntity.ok(response);
    }
}
