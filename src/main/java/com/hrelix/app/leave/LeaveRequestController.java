package com.hrelix.app.leave;

import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(new SuccessResponse<>(true, 200, leaves), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> applyLeave(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequestDto createdLeaveRequest = leaveRequestService.applyLeave(leaveRequest);
        return new ResponseEntity<>(new SuccessResponse<>(true, 201, createdLeaveRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> updateLeaveStatus(
            @PathVariable UUID id,
            @RequestParam LeaveStatus status,
            @RequestParam(required = false) String comments) {
        LeaveRequestDto updatedLeaveRequest = leaveRequestService.updateLeaveStatus(id, status, comments);
        return new ResponseEntity<>(new SuccessResponse<>(true, 201, updatedLeaveRequest), HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse> getEmployeeLeaveRequests(@PathVariable UUID employeeId) {
        List<LeaveRequestDto> leaves = leaveRequestService.getEmployeeLeaveRequests(employeeId);
        return new ResponseEntity<>(new SuccessResponse<>(true, 200, leaves), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLeaveRequestById(@PathVariable UUID id) {
        Optional<LeaveRequestDto> leave = leaveRequestService.getLeaveRequestById(id);
        if (leave.isPresent())
            return new ResponseEntity<>(new SuccessResponse<>(true, 200, leave), HttpStatus.OK);
        else return new ResponseEntity<>(new SuccessResponse<>(true, 404, "Leave Not Found!"), HttpStatus.NOT_FOUND);
    }
}
