package com.hrelix.app.home;

import com.hrelix.app.leave.LeaveRequestDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDto {
    private List<LeaveRequestDto> leaves;
    private List<LeaveRequestDto> pendingLeaves;
    private int totalEmployees;
    private int newEmployees;
    private double payrollProcessed;
    private double payrollPending;
    private double payrollBlocked;
}
