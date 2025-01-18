package com.hrelix.app.leave;

import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leaves")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private MailService mailService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> getAllLeaveRequests() {
        List<LeaveRequestDto> leaves = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "Retrieved all leaves successfully!",
                        leaves
                ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> applyLeave(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequestDto createdLeaveRequest = leaveRequestService.applyLeave(leaveRequest);
        return ResponseEntity.status(201).body(
                new SuccessResponse<>(
                        "Successfully applied for new Leave!",
                        createdLeaveRequest
                ));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> updateLeaveStatus(
            @PathVariable UUID id,
            @RequestParam LeaveStatus status,
            @RequestParam(required = false) String comments) {
        LeaveRequestDto updatedLeaveRequest = leaveRequestService.updateLeaveStatus(id, status, comments);
        return ResponseEntity.status(201).body(
                new SuccessResponse<>(
                        "Updated leave status Successfully!",
                        updatedLeaveRequest
                ));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> getEmployeeLeaveRequests(@PathVariable UUID employeeId) {
        List<LeaveRequestDto> leaves = leaveRequestService.getEmployeeLeaveRequests(employeeId);
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "Retried Successfully!",
                        leaves
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLeaveRequestById(@PathVariable UUID id) {
        Optional<LeaveRequestDto> leave = leaveRequestService.getLeaveRequestById(id);
        if (leave.isPresent())
            return ResponseEntity.ok(
                    new SuccessResponse<>(
                            "Retried Successfully!",
                            leave
                    ));
        else return ResponseEntity.status(404).body(
                new SuccessResponse<>(
                        "Leave with this id not found!",
                        null
                ));
    }
}
