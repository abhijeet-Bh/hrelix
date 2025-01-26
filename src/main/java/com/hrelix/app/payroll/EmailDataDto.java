package com.hrelix.app.payroll;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmailDataDto {
    private String month;
    private String year;
    private String empName;
    private String baseSalary;
    private String allowances;
    private String bonuses;
    private String grossSalary;
    private String taxDeductions;
    private String otherDeductions;
    private String netSalary;
    private String bankName;
    private String accountNumber;
    private String IFSCCode;
    private String transactionID;
    private String creditDateTime;
}
