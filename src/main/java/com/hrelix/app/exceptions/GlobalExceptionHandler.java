package com.hrelix.app.exceptions;

import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle EmployeeNotFoundException
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(404).body(
                new ErrorResponse(
                        "Employee Not Found!",
                        errorDetails
                ));
    }

    // Handle LeaveNotFoundException
    @ExceptionHandler(LeaveNotFoundException.class)
    public ResponseEntity<ApiResponse> handleLeaveNotFoundException(LeaveNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(404).body(
                new ErrorResponse(
                        "Leave Request Not Found!",
                        errorDetails
                ));
    }

    // Handle LoginException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleAuthException(BadCredentialsException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(400).body(
                new ErrorResponse(
                        "Failed to Login, Invalid Password!",
                        errorDetails
                ));
    }

    // Handle Deduction Exception
    @ExceptionHandler(DeductionNotFound.class)
    public ResponseEntity<ApiResponse> handleDeductionNotFoundException(DeductionNotFound ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(404).body(
                new ErrorResponse(
                        "Employee CTC not defined correctly, No deduction definition found!",
                        errorDetails
                ));
    }

    // Handle CTC Not found
    @ExceptionHandler(EmployeeCtcNotFound.class)
    public ResponseEntity<ApiResponse> handleCTCNotFoundException(EmployeeCtcNotFound ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(404).body(
                new ErrorResponse(
                        "Employee CTC not defined correctly, No CTC definition found!",
                        errorDetails
                ));
    }

    // Handle Payroll Not found
    @ExceptionHandler(PayrollNotFound.class)
    public ResponseEntity<ApiResponse> handlePayrollNotFoundException(PayrollNotFound ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(404).body(
                new ErrorResponse(
                        "No Payroll definition found!",
                        errorDetails
                ));
    }

    // Handle Bank Details Not found
    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<ApiResponse> handleBankDetailsNotFoundException(BankAccountNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(404).body(
                new ErrorResponse(
                        "Bank detail not defined!",
                        errorDetails
                ));
    }

    // Handle other RuntimeExceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(Exception ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "An unexpected error occurred: " + ex.getMessage());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(503).body(
                new ErrorResponse(
                        "Something Went Wrong!",
                        errorDetails
                ));
    }
}
