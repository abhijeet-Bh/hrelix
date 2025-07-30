package com.hrelix.app.employee;

import com.hrelix.app.payroll.BankAccountDetail;
import com.hrelix.app.payroll.Deductions;
import com.hrelix.app.payroll.EmployeeCTC;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDetailsDTO {
    private EmployeeDTO employee;
    private EmployeeCTC employeeCTC;
    private Deductions deductions;
    private BankAccountDetail bankAccountDetail;
}
