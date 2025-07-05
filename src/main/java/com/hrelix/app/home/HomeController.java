package com.hrelix.app.home;

import com.hrelix.app.employee.EmployeeService;
import com.hrelix.app.leave.LeaveRequestDto;
import com.hrelix.app.leave.LeaveRequestService;
import com.hrelix.app.payroll.PaymentStatus;
import com.hrelix.app.payroll.Payroll;
import com.hrelix.app.payroll.PayrollService;
import com.hrelix.app.utilities.ApiResponse;
import com.hrelix.app.utilities.SuccessResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/home")
@Hidden
public class HomeController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    LeaveRequestService leaveRequestService;

    @Autowired
    PayrollService payrollService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getHomePageData() {
        int totalEmployee = employeeService.getAllEmployees().size();
        int newEmployee = employeeService.getNewEmployees().size();
        List<LeaveRequestDto> recentLeaves = leaveRequestService.get10LatestLaves();
        List<LeaveRequestDto> pendingLeaves = leaveRequestService.getPendingLeaveRequests();
        int employeesOnLeave = leaveRequestService.getActiveLeaves().size();

        LocalDate today = LocalDate.now();
        List<Payroll> pendingPayrolls = payrollService.getPayrollsByStatus(PaymentStatus.PENDING, today.getMonthValue(), today.getYear());
        List<Payroll> paidPayrolls = payrollService.getPayrollsByStatus(PaymentStatus.PAID, today.getMonthValue(), today.getYear());
        List<Payroll> blockedPayrolls = payrollService.getPayrollsByStatus(PaymentStatus.BLOCKED, today.getMonthValue(), today.getYear());

        double pendingPayment = pendingPayrolls.stream()
                .mapToDouble(Payroll::getNetPayout)
                .sum();
        double paidPayment = paidPayrolls.stream()
                .mapToDouble(Payroll::getNetPayout)
                .sum();
        double blockedPayment = blockedPayrolls.stream()
                .mapToDouble(Payroll::getNetPayout)
                .sum();

        return ResponseEntity.ok(
                new SuccessResponse<HomeDto>(
                        "Home Data Retrieved Successfully!",
                        HomeDto.builder()
                                .totalEmployees(totalEmployee)
                                .newEmployees(newEmployee)
                                .leaves(recentLeaves)
                                .pendingLeaves(pendingLeaves)
                                .payrollProcessed(paidPayment)
                                .payrollPending(pendingPayment)
                                .payrollBlocked(blockedPayment)
                                .employeesOnLeave(employeesOnLeave)
                                .build()
                )
        );
    }
}
