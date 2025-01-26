package com.hrelix.app.leave;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.exceptions.LeaveNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private MailService mailService;

    public List<LeaveRequestDto> getAllLeaveRequests() {
        List<LeaveRequest> leaves = leaveRequestRepository.findAll();
        return leaves.stream()
                .map(leave -> LeaveRequestDto.builder()
                        .id(leave.getId())
                        .employeeId(leave.getEmployee().getId())
                        .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                        .leaveType(leave.getLeaveType())
                        .startDate(leave.getStartDate())
                        .endDate(leave.getEndDate())
                        .status(leave.getStatus())
                        .reason(leave.getReason())
                        .comments(leave.getComments())
                        .build())
                .toList();
    }

    public LeaveRequestDto applyLeave(LeaveRequest leaveRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Employee employee = (Employee) authentication.getPrincipal();
            leaveRequest.setEmployee(employee);
            LeaveRequest leave = leaveRequestRepository.save(leaveRequest);
            mailService.sendLeaveEmail(leave);

            return LeaveRequestDto.builder()
                    .id(leave.getId())
                    .employeeId(leave.getEmployee().getId())
                    .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                    .leaveType(leave.getLeaveType())
                    .startDate(leave.getStartDate())
                    .endDate(leave.getEndDate())
                    .status(leave.getStatus())
                    .reason(leave.getReason())
                    .comments(leave.getComments())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to apply for leave!");
        }

    }

    public LeaveRequestDto updateLeaveStatus(UUID id, LeaveStatus status, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException("Leave request not found with ID: " + id));
        leaveRequest.setStatus(status);
        leaveRequest.setComments(comments);
        try {
            LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
            mailService.sendLeaveEmail(leaveRequest);

            return LeaveRequestDto.builder()
                    .id(updatedLeave.getId())
                    .employeeId(updatedLeave.getEmployee().getId())
                    .employeeName(updatedLeave.getEmployee().getFirstName() + " " + updatedLeave.getEmployee().getLastName())
                    .leaveType(updatedLeave.getLeaveType())
                    .startDate(updatedLeave.getStartDate())
                    .endDate(updatedLeave.getEndDate())
                    .status(updatedLeave.getStatus())
                    .reason(updatedLeave.getReason())
                    .comments(updatedLeave.getComments())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Something went wrong!");
        }
    }

    public List<LeaveRequestDto> getEmployeeLeaveRequests(UUID employeeId) {
        List<LeaveRequest> leaves = leaveRequestRepository.findByEmployeeId(employeeId);
        return leaves.stream()
                .map(leave -> LeaveRequestDto.builder()
                        .id(leave.getId())
                        .employeeId(leave.getEmployee().getId())
                        .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                        .leaveType(leave.getLeaveType())
                        .startDate(leave.getStartDate())
                        .endDate(leave.getEndDate())
                        .status(leave.getStatus())
                        .reason(leave.getReason())
                        .comments(leave.getComments())
                        .build())
                .toList();
    }

    public Optional<LeaveRequestDto> getLeaveRequestById(UUID id) {
        Optional<LeaveRequest> leaveReq = leaveRequestRepository.findById(id);
        if (leaveReq.isPresent()) {
            LeaveRequest leave = leaveReq.get();
            return Optional.ofNullable(LeaveRequestDto.builder()
                    .id(leave.getId())
                    .employeeId(leave.getEmployee().getId())
                    .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                    .leaveType(leave.getLeaveType())
                    .startDate(leave.getStartDate())
                    .endDate(leave.getEndDate())
                    .status(leave.getStatus())
                    .reason(leave.getReason())
                    .comments(leave.getComments())
                    .build());
        } else {
            throw new LeaveNotFoundException("Leave with id: " + id + " Not Found!");
        }
    }
}
