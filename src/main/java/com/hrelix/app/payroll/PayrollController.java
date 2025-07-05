package com.hrelix.app.payroll;

import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Autowired
    private BankAccountDetailService bankAccountDetailService;

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

    @PostMapping("/set-bank-details")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> createEmployeeBankDetails(@RequestBody BankAccountDetail accountDetail) {
        BankAccountDetail createdAccount = bankAccountDetailService.createBankDetails(accountDetail);
        SuccessResponse<BankAccountDetail> response = new SuccessResponse<>(
                "Employee Bank Details saved successfully",
                createdAccount
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
                "All Payrolls retrieved successfully",
                allPayrolls
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getPayrollsByStatus(
            @RequestParam(required = false) PaymentStatus query,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        List<Payroll> allPayrolls = payrollService.getPayrollsByStatus(query, month, year);
        SuccessResponse<List<Payroll>> response = new SuccessResponse<>(
                "Payrolls by status retrieved successfully",
                allPayrolls
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{payrollId}")
    public ResponseEntity<ApiResponse> getPayrollById(@PathVariable UUID payrollId) {
        Payroll payroll = payrollService.getPayrollById(payrollId);
        SuccessResponse<Payroll> response = new SuccessResponse<>(
                "Payroll by ID retrieved successfully",
                payroll
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse> getPayrollsByEmployeeId(@PathVariable UUID employeeId) {
        List<Payroll> payroll = payrollService.getPayrollByEmployeeId(employeeId);
        SuccessResponse<List<Payroll>> response = new SuccessResponse<>(
                "Payrolls by employee retrieved successfully",
                payroll
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/pay/{payrollId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> processPayment(
            @PathVariable UUID payrollId,
            @RequestBody Map<String, Object> payload
    ) {
        String testEmail = (String) payload.get("testEmail");
        String txnNum = (String) payload.get("txnNum");
        if (txnNum == null)
            throw new RuntimeException("Transaction number is compulsory");

        Payroll payroll = payrollService.processPayment(payrollId, testEmail, txnNum);
        SuccessResponse<Payroll> response = new SuccessResponse<>(
                "Payrolls processed successfully!",
                payroll
        );
        return ResponseEntity.ok(response);
    }
}
