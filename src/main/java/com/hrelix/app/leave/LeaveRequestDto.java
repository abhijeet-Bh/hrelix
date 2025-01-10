package com.hrelix.app.leave;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
public class LeaveRequestDto {
    private UUID id;
    private UUID employeeId;
    private String employeeName;

    private LeaveType leaveType;

    private LocalDate startDate;
    private LocalDate endDate;

    private LeaveStatus status;

    private String reason;

    private String comments;
}
