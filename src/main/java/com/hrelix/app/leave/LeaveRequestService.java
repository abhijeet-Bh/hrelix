package com.hrelix.app.leave;

import com.hrelix.app.employee.Employee;
import com.hrelix.app.exceptions.LeaveNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Page<LeaveRequestDto> getAllLeaveRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<LeaveRequest> leaves = leaveRequestRepository.findAll(pageable);

        return leaves.map(leave -> LeaveRequestDto.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployee().getId())
                .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                .leaveType(leave.getLeaveType())
                .appliedOn(LocalDate.from(leave.getCreatedAt()))
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .status(leave.getStatus())
                .reason(leave.getReason())
                .comments(leave.getComments())
                .build()
        );
    }


    public List<LeaveRequestDto> get10LatestLaves() {
        List<LeaveRequest> leaves = leaveRequestRepository.findTop10ByOrderByCreatedAtDesc();
        return leaves.stream()
                .map(leave -> LeaveRequestDto.builder()
                        .id(leave.getId())
                        .employeeId(leave.getEmployee().getId())
                        .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                        .leaveType(leave.getLeaveType())
                        .appliedOn(LocalDate.from(leave.getCreatedAt()))
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
                    .appliedOn(LocalDate.from(leave.getCreatedAt()))
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
                    .appliedOn(LocalDate.from(updatedLeave.getCreatedAt()))
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
                        .appliedOn(LocalDate.from(leave.getCreatedAt()))
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
                    .appliedOn(LocalDate.from(leave.getCreatedAt()))
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

    public List<LeaveRequestDto> getPendingLeaveRequests() {
        List<LeaveRequest> leaves = leaveRequestRepository.findByStatus(LeaveStatus.PENDING);
        return leaves.stream()
                .map(leave -> LeaveRequestDto.builder()
                        .id(leave.getId())
                        .employeeId(leave.getEmployee().getId())
                        .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                        .leaveType(leave.getLeaveType())
                        .appliedOn(LocalDate.from(leave.getCreatedAt()))
                        .startDate(leave.getStartDate())
                        .endDate(leave.getEndDate())
                        .status(leave.getStatus())
                        .reason(leave.getReason())
                        .comments(leave.getComments())
                        .build())
                .toList();
    }

    public List<LeaveRequestDto> getActiveLeaves() {
        LocalDate today = LocalDate.now();
        List<LeaveRequest> leaves = leaveRequestRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                LeaveStatus.APPROVED,
                today,
                today
        );
        return leaves.stream()
                .map(leave -> LeaveRequestDto.builder()
                        .id(leave.getId())
                        .employeeId(leave.getEmployee().getId())
                        .employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
                        .leaveType(leave.getLeaveType())
                        .appliedOn(LocalDate.from(leave.getCreatedAt()))
                        .startDate(leave.getStartDate())
                        .endDate(leave.getEndDate())
                        .status(leave.getStatus())
                        .reason(leave.getReason())
                        .comments(leave.getComments())
                        .build())
                .toList();
    }
}
