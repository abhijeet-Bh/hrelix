package com.hrelix.app.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
public class Payroll {
    @Id
    @GeneratedValue
    private UUID payrollId;

    private UUID employee;
    private String monthYear;
    private double netCTC;
    private double netDeductions;
    private double netPayout;
    private PaymentStatus status;
    private LocalDate date;
}
